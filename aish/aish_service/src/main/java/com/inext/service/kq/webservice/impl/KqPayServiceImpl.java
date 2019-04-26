package com.inext.service.kq.webservice.impl;

import com.bill99.asap.exception.CryptoException;
import com.bill99.schema.ddp.product.*;
import com.bill99.seashell.common.util.DateUtil;
import com.inext.dto.AssetRepaymentOrderByWithholdDto;
import com.inext.entity.*;
import com.inext.enums.PPDStatusEnum;
import com.inext.enums.RepaymentLockMsgEnum;
import com.inext.ppd.PpdDebitItem;
import com.inext.ppd.ReturnCode;
import com.inext.ppd.XmlPacket;
import com.inext.service.*;
import com.inext.service.handler.invocation.DaiKouRedisKey;
import com.inext.service.kq.webservice.KqPaySevice;
import com.inext.utils.DateUtils;
import com.inext.utils.RedisUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class KqPayServiceImpl implements KqPaySevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(KqPayServiceImpl.class);

    @Autowired
    private Bill99DDPRealTimeService bill99DDPRealTimeService;
    @Autowired
    private IAssetRepaymentOrderService assetRepaymentOrderService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    IAssetBorrowOrderService borrowOrderService;
    @Autowired
    IAssetOrderStatusHisService orderStatusHisService;
    @Autowired
    IAssetRepaymentOrderDetailService repaymentOrderDetailService;
    @Autowired
    IBorrowUserService borrowUserService;
    @Autowired
    private IBackConfigParamsService backConfigParamsService;

    /***
     * 免签约批量
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    @Override
    public void advDebitBatch() {
        String dKRedisKey = DaiKouRedisKey.DK.getCode();

        if (redisUtil.get(dKRedisKey) != null) {
            LOGGER.error("doDKPay 正在跑.." + dKRedisKey);
            return;
        }
        try {
            redisUtil.set(dKRedisKey, dKRedisKey, 60 * 60l);//过期时间一个小时
            Map<String, Object> map = new HashMap<>();
            map.put("repaymentTime", new Date());
            List<AssetRepaymentOrderByWithholdDto> list = assetRepaymentOrderService.getListByWithhold(map);
            if (list == null || list.size() == 0) {
                return;
            }

            String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
            long timeout = RedisUtil.ORDER_PAY_LOCK_TIMEOUT;
            List<AssetRepaymentOrderByWithholdDto> lists = list.stream().filter(p -> {
                //过滤招商银行卡
                if(p.getCardType()==3){
                    return false;
                }
                //判断当前订单是否还清、逾期还清
                List<Integer> orderStatuses = Arrays.asList(
                        AssetOrderStatusHis.STATUS_YHK);

                if (orderStatuses.contains(p.getOrderStatus().intValue())) {
                    LOGGER.info("扣款:当前订单已经还清  还款id=" + p.getId());
                    return false;
                }
                Integer repaymentId = p.getId();
                String lockKey = RedisUtil.ORDER_PAY_SUBFIX + repaymentId;
                //拿锁  拿到锁则可以代扣，否则不可以代扣
                if (!redisUtil.lock(lockKey, time)) {
                    String msg = "订单正在支付处理中";
                    Object value = redisUtil.get(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG);
                    if (value != null) {
                        msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
                    }
                    LOGGER.info("doDKPayTask 当前订单{},已经在进行其它方式还款:{}", lockKey, msg);
                    return false;
                }
                //设置锁 消息提示
                redisUtil.set(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG, RepaymentLockMsgEnum.SYSTEM_DK.getValue(), timeout / 1000);
                return true;
            }).collect(Collectors.toList());
            MerchantDebitRequest request = new MerchantDebitRequest();
            BigDecimal amountTotal = new BigDecimal(0);
            Map<String, String> kq = backConfigParamsService.getBackConfig(BackConfigParams.KQ, null);
            String kqContractId = kq.get("contract_id");
            String kqMemberCode = kq.get("member_code");
            String kqMerchantAcctId = kq.get("merchant_acctid");
            request.setBgUrl("http://www.99bill.com");
            request.setContractId(kqContractId);//"1565646";
            request.setExt1("");
            request.setExt2("");
            request.setInputCharset("1");
            request.setMemberCode(kqMemberCode);
            request.setMerchantAcctId(kqMerchantAcctId);//("1001213884201");
            request.setRequestTime(DateUtil.formatDateTime("yyyyMMddHHmmss"));

            Map<String, List<AssetRepaymentOrderByWithholdDto>> mapOrder =
                    new HashMap<String, List<AssetRepaymentOrderByWithholdDto>>();
            List<AssetRepaymentOrderByWithholdDto> empList = new ArrayList<>();
            int size = lists.size();
            for (int i = 0; i < size; i++) {
                empList.add(lists.get(i));
                if ((i != 0 && i % 199 == 0) || size == (i + 1)) {
                    mapOrder.put("A" + i, empList);
                    empList = new ArrayList<AssetRepaymentOrderByWithholdDto>();
                }
            }
            mapOrder.forEach((key, value) -> {
                List<MerchantDebitRequestItem> items = getMerchantDebitRequestItem(value, request);
                request.setItems(items);
                try {
                    String resp = bill99DDPRealTimeService.advDebitBatch(request);
                    batchResult(resp, value);
                } catch (Exception e) {
                    LOGGER.error("KqPayServiceImpl >>> advDebitBatch 扣款处理结果异常:{}", ExceptionUtils.getStackTrace(e));
                } finally {
                    value.stream().map(p -> {
                        redisUtil.unlock(RedisUtil.ORDER_PAY_SUBFIX + p.getId());
                        return p;
                    }).collect(Collectors.toList());
                }
            });
        } catch (Exception e) {
            redisUtil.remove(dKRedisKey);
            LOGGER.info("KqPayServiceImpl >>> advDebitBatch 扣款处理异常:{}", e.getMessage());
        } finally {
            redisUtil.remove(dKRedisKey);
        }
    }

    private List<MerchantDebitRequestItem> getMerchantDebitRequestItem
            (List<AssetRepaymentOrderByWithholdDto> list, MerchantDebitRequest request) {
        List<MerchantDebitRequestItem> items = new ArrayList<MerchantDebitRequestItem>();
        int size = list.size();
        Double amount = list.stream().mapToDouble(p -> p.getRepaymentAmount().doubleValue()).sum();
        List<Integer> idList = list.stream().map(p -> p.getId()).collect(Collectors.toList());
        request.setAmountTotal(amount + "");
        request.setNumTotal(size + "");
        Date date = new Date();
        String dateStr = DateUtils.dateToStrss(date);
        String requestid = "ASH" + dateStr;
        request.setRequestId(requestid);
        AssetRepaymentOrderWithhold withhold = new AssetRepaymentOrderWithhold();
        withhold.setCreateTime(date);
        withhold.setRequestId(requestid);
        withhold.setChargeStatus(0);
        withhold.setUpdateTime(date);
        withhold.setReqNum(size);
        withhold.setReqAmt(new BigDecimal(amount));
        int withholdId = assetRepaymentOrderService.insertOrderWithhold(withhold);
        if (withholdId <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            String reqid = "KQH" + DateUtils.dateToStr(date) + i;
            AssetRepaymentOrderByWithholdDto dto = list.get(i);
            MerchantDebitRequestItem item = new MerchantDebitRequestItem();
            // 商家订单号DateUtil.formatDateTime("yyyyMMddHHmmss")
            item.setSeqId(reqid);
            item.setUsage("水费"); // 约定业务
            item.setBankId(dto.getBankCode()); // 银行代码 "BCOM"
            item.setAccType("0201"); // 付款账户类型
            item.setOpenAccDept(dto.getBankName()); // 付款账户开户机构名
            item.setBankAcctName(dto.getUserName()); // 付款方银行账户名称
            item.setBankAcctId(dto.getCardCode()); // 付款方账号
            item.setExpiredDate(""); // 有效期
            item.setMobile(dto.getUserPhone());
            item.setIdType("101"); // 付款人在开户行提供的证件类型
            item.setIdCode(dto.getUserCardNo()); // 付款人在开户行提供的证件号码
            item.setAmount(dto.getRepaymentAmount() + ""); // 收款金额
            item.setCurType("CNY"); // 币种
            item.setNote("花小侠"); // 附言
            item.setRemark("花小侠"); // 备注
            items.add(item);

            AssetRepaymentOrder updateRepaymentOrder = new AssetRepaymentOrder();
            updateRepaymentOrder.setId(dto.getId());
            updateRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_DOING);
            updateRepaymentOrder.setUpdateTime(new Date());
            updateRepaymentOrder.setSeqId(reqid);
            updateRepaymentOrder.setWithholdId(withhold.getId());
            assetRepaymentOrderService.update(updateRepaymentOrder);
        }
        return items;
    }

    /**
     * 协议批量
     *
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    public void testSignDebitBatch() throws JiBXException, IOException, CryptoException {
        MerchantProtocalDebitRequest request = new MerchantProtocalDebitRequest();
        request.setInputCharset("1");
        request.setMemberCode("10012138842");
        request.setMerchantAcctId("1001213884201");
        request.setContractId("1565646");
        request.setRequestId(System.currentTimeMillis() + "");
        request.setRequestTime(DateUtil.formatDateTime("yyyyMMddHHmmss"));
        request.setNumTotal("1");
        request.setAmountTotal("0.01");
        request.setExt1("");
        request.setExt2("");

        List<MerchantProtocalDebitRequestItem> items = getMerchantSignDebitRequestItem();
        request.setItems(items);
        String resp = bill99DDPRealTimeService.signDebitBatch(request);

    }

    private List<MerchantProtocalDebitRequestItem> getMerchantSignDebitRequestItem() {
        List<MerchantProtocalDebitRequestItem> items = new ArrayList<MerchantProtocalDebitRequestItem>();
        for (int i = 0; i < 1; i++) {
            MerchantProtocalDebitRequestItem item = new MerchantProtocalDebitRequestItem();
            item.setMechantcontractno("ICBCGX");//授权协议号
            item.setSeqId(DateUtil.formatDateTime("yyyyMMddHHmmss") + i); // 商家订单号
            item.setBankAcctName("fff"); // 付款方银行账户名称
            item.setBankAcctId("555666"); // 付款方账号
            item.setAmount("0.01"); // 收款金额
            item.setCurType("CNY"); // 币种
            item.setUsage("保险费续交"); // 约定业务
            item.setRemark("保险费续交"); // 备注
            items.add(item);
        }
        return items;
    }


    /**
     * 明细查询
     *
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void pkiQuery(AssetRepaymentOrderWithhold withhold){
        Map<String, String> kq = backConfigParamsService.getBackConfig(BackConfigParams.KQ, null);
        String kqContractId = kq.get("contract_id");
        String kqMemberCode = kq.get("member_code");
        String kqMerchantAcctId = kq.get("merchant_acctid");
        MerchantDebitQueryRequest req = new MerchantDebitQueryRequest();
        req.setInputCharset("1");
        req.setMemberCode(kqMemberCode);
        req.setMerchantAcctId(kqMerchantAcctId);// 会员编号+01
        req.setRequestTime(DateUtil.formatDateTime("yyyyMMddHHmmss"));
        req.setBatchId(withhold.getRequestId());//20130318162540 请求批次号
        //req.setSeqId("201602291600381");
        //还可以按照时间查询，不按商家订单号查询。这样返回结果需要分页
        //req.setStarttime("20130222110000");
        //req.setEndtime("20130222111110");
        try {
            String resp = bill99DDPRealTimeService.query(req);
            System.out.println(resp);
            debitQueryResult(resp, withhold);
        }catch (Exception e){
            LOGGER.error("KqPayServiceImpl  >>> pkiQuery 扣款明细查询异常：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 批次查询
     *
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    @Override
    public void pkiBatchQuery() {
        String dKQueryRedisKey = DaiKouRedisKey.DK_QUERY.getCode();

        if (redisUtil.get(dKQueryRedisKey) != null) {
            LOGGER.info("doDKQueryPay 正在跑.." + dKQueryRedisKey);
            return;
        }

        try {
            redisUtil.set(dKQueryRedisKey, dKQueryRedisKey, 60 * 60l);//过期时间一个小时

            List<AssetRepaymentOrderWithhold> list = assetRepaymentOrderService.getWithholdByStatus(0);
            if (list == null || list.size() == 0) {
                return;
            }
            Map<String, String> kq = backConfigParamsService.getBackConfig(BackConfigParams.KQ, null);
            String kqContractId = kq.get("contract_id");
            String kqMemberCode = kq.get("member_code");
            String kqMerchantAcctId = kq.get("merchant_acctid");
            MerchantDebitBatchQueryRequest req = new MerchantDebitBatchQueryRequest();
            req.setInputCharset("1");
            req.setMemberCode(kqMemberCode);  //
            req.setMerchantAcctId(kqMerchantAcctId);
            req.setRequestTime(DateUtil.formatDateTime("yyyyMMddHHmmss"));
            req.setExt1("");
            req.setExt2("");

            String time = System.currentTimeMillis() + RedisUtil.ORDER_QUERY_LOCK_TIMEOUT + "";
            long timeout = RedisUtil.ORDER_QUERY_LOCK_TIMEOUT;
            List<AssetRepaymentOrderWithhold> lists= list.stream().filter(p->{
                String lockKey = RedisUtil.ORDER_QUERY_SUBFIX + p.getRequestId();
                //拿锁  拿到锁则可以代扣，否则不可以代扣
                if (!redisUtil.lock(lockKey, time)) {
                    String msg = "订单正在查询处理中";
                    Object value = redisUtil.get(lockKey + RedisUtil.ORDER_QUERY_LOCK_MSG);
                    if (value != null) {
                        msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
                    }
                    LOGGER.info("doDKPayTask 当前订单{},已经在进行其它方式查询处理:{}", lockKey, msg);
                    return false;
                }
                //设置锁 消息提示
                redisUtil.set(lockKey + RedisUtil.ORDER_QUERY_LOCK_MSG, RepaymentLockMsgEnum.SYSTEM_DK.getValue(), timeout / 1000);
                return true;
            }).map(p->{
                try {
                    req.setBatchId(p.getRequestId());//20130318162540 请求批次号
                    String resp = bill99DDPRealTimeService.batchQuery(req);
                    if (debitbatchQueryResult(resp, p)) {
                        pkiQuery(p);
                    }
                }catch (Exception e) {
                    LOGGER.error("KqPayServiceImpl  >>> pkiBatchQuery扣款查询异常：{}", e.getMessage());
                }finally {
                    redisUtil.unlock(RedisUtil.ORDER_QUERY_SUBFIX + p.getRequestId());
                }
                return p;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("KqPayServiceImpl  >>> pkiBatchQuery扣款查询异常：{}", e.getMessage());
            redisUtil.remove(dKQueryRedisKey);
        } finally {
            redisUtil.remove(dKQueryRedisKey);
        }

    }

    //扣款结果处理
    public void batchResult(String resp, List<AssetRepaymentOrderByWithholdDto> list) {
        XmlPacket pktRsp = XmlPacket.valueOf(resp);
        Map<String, Object> bMap = pktRsp.resultBatch(pktRsp.getEntity());
        if (ReturnCode.PPD_FAIL.getCode().equals(bMap.get("dealResult").toString())) {

            AssetRepaymentOrderWithhold withhold = assetRepaymentOrderService.getWithholdByOther(bMap.get("requestId").toString(), null);
            if (withhold != null) {
                assetRepaymentOrderService.updateWithhold(bMap.get("batchId") == null ? null : bMap.get("batchId").toString(),
                        1, new Date(), bMap.get("batchErrMessage") == null ? null :
                                bMap.get("batchErrMessage").toString(), withhold.getId());
                List<Integer> idList = list.stream().map(p -> p.getId()).collect(Collectors.toList());
                assetRepaymentOrderService.updateRepaymentByIdWithhold(withhold.getId(), 3,
                        5, bMap.get("batchErrMessage") == null ? null :
                                bMap.get("batchErrMessage").toString(), idList);
            }
        } else {
            AssetRepaymentOrderWithhold withhold = assetRepaymentOrderService.getWithholdByOther(bMap.get("requestId").toString(), null);
            assetRepaymentOrderService.updateWithhold(bMap.get("batchId").toString(), null, null, null, withhold.getId());
        }
    }

    //批次结果处理
    public boolean debitbatchQueryResult(String resp, AssetRepaymentOrderWithhold withhold) {
        XmlPacket pktRsp = XmlPacket.valueOf(resp);
        Map<String, Object> bMap = pktRsp.resultM(pktRsp.getEntity());
        if(bMap ==null || bMap.size()==0){
            return false;
        }
        if (ReturnCode.PPD_SUCCESS.getCode().equals(bMap.get("batchResult").toString()) && Integer.parseInt(bMap.get("successNum").toString()) > 0) {
            int result = assetRepaymentOrderService.updateWithhold(null,
                    2, new Date(),
                    bMap.get("errMessage") == null ? ReturnCode.getByCode(bMap.get("batchResult").toString()).getDesc() :
                            bMap.get("errMessage").toString(), withhold.getId());
            return true;

        } else if((ReturnCode.PPD_SUCCESS.getCode().equals(bMap.get("batchResult").toString()) && Integer.parseInt(bMap.get("successNum").toString()) == 0)){
            int result = assetRepaymentOrderService.updateWithhold(null,
                    1, new Date(),
                    bMap.get("errMessage") == null ? ReturnCode.getByCode(bMap.get("batchResult").toString()).getDesc() :
                            bMap.get("errMessage").toString(), withhold.getId());
            return true;
        }else if (ReturnCode.PPD_FAIL.getCode().equals(bMap.get("batchResult").toString())) {
            int result = assetRepaymentOrderService.updateWithhold(null,
                    1, new Date(),
                    bMap.get("errMessage") == null ? ReturnCode.getByCode(bMap.get("batchResult").toString()).getDesc() :
                            bMap.get("errMessage").toString(), withhold.getId());
            if (result > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("withholdId", withhold.getId());
                List<AssetRepaymentOrder> list = assetRepaymentOrderService.getListByRapaymentOrder(map);
                List<Integer> idList = list.stream().map(p -> p.getId()).collect(Collectors.toList());
                assetRepaymentOrderService.updateRepaymentByIdWithhold(withhold.getId(), AssetRepaymentOrder.DK_PAY_FAILED,
                        null, bMap.get("batchErrMessage") == null ? null :
                                bMap.get("batchErrMessage").toString(), idList);
            }

        }
        return false;
    }

    //明细结果处理
    public boolean debitQueryResult(String resp, AssetRepaymentOrderWithhold withhold) {
        XmlPacket pktRsp = XmlPacket.valueOf(resp);
        List<PpdDebitItem> resultItems = pktRsp.resultItems(pktRsp.getData());
        if(resultItems==null || resultItems.size()==0){
            return false;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("withholdId", withhold.getId());
        params.put("dkPayStatus", 1);
        List<AssetRepaymentOrder> repaymentOrders = assetRepaymentOrderService.getListByRapaymentOrder(params);
        if (repaymentOrders == null || repaymentOrders.size() == 0) {
            return false;
        }
        Map<String, AssetRepaymentOrder> maps = repaymentOrders.stream().
                collect(Collectors.toMap(AssetRepaymentOrder::getSeqId, dto -> dto));
        resultItems.forEach(p -> {
            if (maps.containsKey(p.getSeqId())) {
                AssetRepaymentOrder repaymentOrder = maps.get(p.getSeqId());
                if (PPDStatusEnum.KK_SUCCESS.getCode().equals(p.getDealResult())) {
                    String amount = p.getAmount();
                    //借款表
                    AssetBorrowOrder borrowOrder = borrowOrderService.getOrderById(repaymentOrder.getOrderId());
                    //还款状态
                    Integer status = AssetOrderStatusHis.STATUS_YHK;
                    //还款成功记录
                    AssetOrderStatusHis his = new AssetOrderStatusHis();

                    his.setOrderId(borrowOrder.getId());
                    his.setOrderStatus(status);
//                    if (repaymentOrder.getLateDay() > 0) {
//                        his.setRemark("已取消交易，返还预付款+违约金+滞纳金，共" + new BigDecimal(amount) + "元");
//                    } else {
//                        his.setRemark("已取消交易，返还预付款+违约金，共" + new BigDecimal(amount) + "元");
//                    }
                    his.setRemark(AssetOrderStatusHis.orderStatusMap.get(status));
                    his.setCreateTime(new Date());
                    orderStatusHisService.saveHis(his);

                    //更新借款表信息封装
                    AssetBorrowOrder updateOrder = new AssetBorrowOrder();
                    updateOrder.setId(borrowOrder.getId());
                    updateOrder.setStatus(status);
                    updateOrder.setOrderEnd(1);//取消关闭
                    updateOrder.setUpdateTime(new Date());
                    borrowOrderService.updateByPrimaryKeySelective(updateOrder);

                    AssetRepaymentOrder updateRepaymentOrder = new AssetRepaymentOrder();
                    Date now = new Date();

                    updateRepaymentOrder.setId(repaymentOrder.getId());
                    updateRepaymentOrder.setOrderStatus(status);
                    updateRepaymentOrder.setUpdateTime(now);
                    updateRepaymentOrder.setRepaymentRealTime(now);
                    //Integer repaymentType = PaymentTypeEnum.YOP_DK.getValue();
                    //updateRepaymentOrder.setRepaymentType(repaymentType);
                    //三方支付成功状态
                    updateRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_SUCCESS);
                    //上次已还款金额
                    //BigDecimal lastRepaymentedAmount = repaymentOrder.getRepaymentedAmount();

                    //LOGGER.info("易宝代扣扣款---AssetRepaymentOrder--订单号为{},还款状态为：{},上次已还款金额：{}元，本次还款：{}元", repaymentOrder.getId(), status, lastRepaymentedAmount, new BigDecimal(amount));

                    //本次还款金额
                    updateRepaymentOrder.setRepaymentedAmount(new BigDecimal(amount));
                    updateRepaymentOrder.setUpdateTime(new Date());

                    assetRepaymentOrderService.update(updateRepaymentOrder);

                    // 2018-07-02 当用户成功还款后 将用户标识更新为老用户  那么当用户新创建一笔订单时会使用到这个字段标识该笔订单信息
                    BorrowUser user = new BorrowUser();
                    user.setId(borrowOrder.getUserId());
                    user.setIsOld(1);
                    borrowUserService.updateUser(user);

                    /*
                     *增加还款流水详情
                     */
                    repaymentOrderDetailService.insertAssetRepaymentOrderDetail(new BigDecimal(amount), new BigDecimal(0), repaymentOrder, status, "块钱代扣", null, repaymentOrder.getSeqId(), null);
                } else if (PPDStatusEnum.KK_FAIL.getCode().equals(p.getDealResult()) ||
                        PPDStatusEnum.CHECK_FAIL.getCode().equals(p.getDealResult()) ||
                        PPDStatusEnum.REVIEW_FAIL.getCode().equals(p.getDealResult()) ||
                        PPDStatusEnum.MESSAGE_FAIL.getCode().equals(p.getDealResult())) {
                    AssetRepaymentOrder updateRepaymentOrder = new AssetRepaymentOrder();
                    updateRepaymentOrder.setId(repaymentOrder.getId());
                    updateRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_FAILED);
                    updateRepaymentOrder.setUpdateTime(new Date());
                    updateRepaymentOrder.setErrMessage(repaymentOrder.getErrMessage()+"_"+ repaymentOrder.getSeqId()+"&&"+repaymentOrder.getWithholdId()+"&&" + p.getErrMessage());
                    assetRepaymentOrderService.update(updateRepaymentOrder);
                }
            }
        });
        return true;
    }

}

