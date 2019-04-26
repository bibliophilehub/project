package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.constants.SmsContentConstant;
import com.inext.dao.IBankAllInfoDao;
import com.inext.dao.RepaymentInfoDao;
import com.inext.entity.*;
import com.inext.entity.fuyou.Fuiou;
import com.inext.entity.heli.BindCardPayResponseVo;
import com.inext.entity.heli.BindCardPayVo;
import com.inext.entity.heli.BindCardResponseVo;
import com.inext.enums.RepaymentLockMsgEnum;
import com.inext.service.*;
import com.inext.utils.DateUtil;
import com.inext.utils.RedisUtil;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.baofoo.util.HttpUtil;
import com.inext.utils.helipay.*;
import com.inext.utils.newbaofoo.rsa.RsaCodingUtil;
import com.inext.utils.newbaofoo.rsa.SecurityUtil;
import com.inext.utils.newbaofoo.rsa.SignatureUtils;
import com.inext.utils.newbaofoo.util.FormatUtil;
import com.inext.utils.sms.SmsSendUtil;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: jzhang
 * @Date: 2018-04-14 0014 下午 02:17
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RepaymentServiceImpl implements IRepaymentService {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(RepaymentServiceImpl.class);
    @Resource
    IBorrowUserService iBorrowUserService;
    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    RepaymentInfoDao repaymentInfoDao;
    @Resource
    IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;
    @Resource
    IAssetRenewalOrderService iAssetRenewalOrderService;
    @Autowired
    IBankAllInfoDao bankAllInfoDao;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Fuiou getRepaymentFromParams(Integer userId, Integer orderId, Integer type) {
        BorrowUser user = iBorrowUserService.getBorrowUserById(userId);
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);
        RepaymentInfo info = new RepaymentInfo();
        info.setAssetId(order.getId());
        if (type != null) {
            switch (type) {
                case 1:
                    info.setReqAmt(assetRepaymentOrder.getRepaymentAmount().subtract(assetRepaymentOrder.getRepaymentedAmount()));
                    break;
                case 2:
                    info.setReqAmt(order.getPenaltyAmount());
                    break;
            }
        }
        info.setType(type);
        repaymentInfoDao.insertSelective(info);
        user.setCardCode(order.getUserCardCode());
        return new Fuiou(user, info);
    }

    @Override
    public int doCcallback(Map<String, String> params) {
        String responseCode = params.get("RESPONSECODE");//响应代码
        String responseMsg = params.get("RESPONSEMSG");//响应消息
        String repaymentInfoId = params.get("MCHNTORDERID").substring(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY").length());//商户订单号

        String fyOrderId = params.get("ORDERID");//富友订单号
        String amt = params.get("AMT");//交易金额
        int isSuc = 2;
        RepaymentInfo info = new RepaymentInfo();
        info.setId(Integer.parseInt(repaymentInfoId));
        info = repaymentInfoDao.selectOne(info);
        if(info.getIsSuc().equals(1)){//如果已经成功了 就直接放回
            return 1;
        }
        //当前还款订单是否处于支付中
        long timeout = 60*1000;
        final String lockKey = RedisUtil.LOCK_PAY_CCALLBACK + repaymentInfoId;
        final String time = System.currentTimeMillis() + 60*1000 + "";// 1分钟
        //拿锁 能拿到锁 则可以进行下面操作
        if (!redisUtil.lock(lockKey, time))
        {
            //订单正在处理中
            return 1;
        }
        //设置锁 消息提示
        redisUtil.set(lockKey, "订单正在处理中", timeout);

        info.setOrderId(fyOrderId);
        info.setResAmt(new BigDecimal(amt));
        isSuc = responseCode.equals("0000") ? 1 : 2;
//        isSuc=info.getReqAmt().compareTo(info.getResAmt())==0?1:2;
        info.setIsSuc(isSuc);

        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(info.getAssetId());
        AssetOrderStatusHis his = new AssetOrderStatusHis();
        his.setOrderId(order.getId());

        AssetRepaymentOrder tem = iAssetRepaymentOrderService.getRepaymentByOrderId(order.getId());
        if (isSuc == 1) {
            if (info.getType() != null) {
                switch (info.getType()) {
                    case 1:
                        order.setStatus(AssetOrderStatusHis.STATUS_YHK); //1 审核中 2 待打款 3 待寄出 5 审核拒绝 7 已违约 8 已取消 10 已寄出 11 已完成
                        his.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
//                        if (order.getLateDay() > 0) {
//                            his.setRemark("已取消交易，返还预付款+违约金+滞纳金，共" + info.getResAmt() + "元");
//                        } else {
//                            his.setRemark("已取消交易，返还预付款+违约金，共" + info.getResAmt() + "元");
//                        }
//                        his.setCreateTime(new Date());
//                        iAssetOrderStatusHisService.saveHis(his);

                        his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_YHK));
                        order.setOrderEnd(1);//取消关闭
//                        SmsSendUtil.sendSmsDiy(order.getUserPhone(), SmsContentConstant.CANCEL_ORDER);
                        SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(order.getUserPhone(), SmsContentConstant.CANCEL_ORDER);
                        AssetRepaymentOrder assetRepaymentOrder = new AssetRepaymentOrder();
                        assetRepaymentOrder.setId(tem.getId());
                        assetRepaymentOrder.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
                        assetRepaymentOrder.setUpdateTime(new Date());
                        assetRepaymentOrder.setRepaymentRealTime(new Date());
                        assetRepaymentOrder.setRepaymentType(AssetRepaymentOrder.REPAYMENT_TYPE_INITIATIVE);
                        if(tem.getRepaymentedAmount()==null){
                            assetRepaymentOrder.setRepaymentedAmount(info.getReqAmt());
                        }else{
                            assetRepaymentOrder.setRepaymentedAmount(info.getReqAmt().add(tem.getRepaymentedAmount()));
                        }
                        iAssetRepaymentOrderService.update(assetRepaymentOrder);

                        BorrowUser user =new BorrowUser();
                        user.setId(tem.getUserId());
                        user.setIsOld(1);
                        iBorrowUserService.updateUser(user);
                        break;
                    case 2:
                        Integer renewalDay = order.getMoneyDay();

                        order.setLoanEndTime(DateUtil.addDay(tem.getRepaymentTime(), renewalDay));

                        his.setOrderStatus(AssetOrderStatusHis.STATUS_YXQ);
                        his.setRemark("已续期，\n" +
                                DateUtil.formatDate("yyyy年MM月dd日 HH:mm:ss", order.getLoanEndTime()) +
                                "前未还款，则视为逾期，平台将收取逾期利息。");
                        AssetRenewalOrder assetRenewalOrder = new AssetRenewalOrder();
                        assetRenewalOrder.setUserId(order.getUserId());
                        assetRenewalOrder.setUserPhone(order.getUserPhone());
                        assetRenewalOrder.setUserName(order.getUserName());
                        assetRenewalOrder.setOrderId(order.getId());
                        assetRenewalOrder.setMoneyAmount(order.getPerPayMoney());
                        assetRenewalOrder.setPenaltyAmount(order.getPenaltyAmount());
                        assetRenewalOrder.setMoneyDay(order.getMoneyDay());
                        assetRenewalOrder.setCreditRepaymentTime(tem.getCreditRepaymentTime());
                        assetRenewalOrder.setRenewalTime(new Date());
                        assetRenewalOrder.setRenewalDay(renewalDay);
                        assetRenewalOrder.setRepaymentTime(DateUtil.addDay(tem.getRepaymentTime(), renewalDay));
                        iAssetRenewalOrderService.insertRenewalOrder(assetRenewalOrder);

                        AssetRepaymentOrder repay = new AssetRepaymentOrder();
                        repay.setId(tem.getId());
                        repay.setUpdateTime(new Date());
                        repay.setOrderRenewal(AssetOrderStatusHis.STATUS_YXQ);
                        repay.setRepaymentTime(DateUtil.addDay(tem.getRepaymentTime(), renewalDay));
                        iAssetRepaymentOrderService.update(repay);

                        break;
                }
            }
        } else {
            if (order.getLateDay() > 0) {
//                order.setStatus(AssetOrderStatusHis.STATUS_YYQ);
                his.setOrderStatus(AssetOrderStatusHis.STATUS_YYQ);
                his.setRemark("支付失败(" + responseMsg + ")！已逾期");
            } else {
//                order.setStatus(AssetOrderStatusHis.STATUS_FKCG);
                his.setOrderStatus(AssetOrderStatusHis.STATUS_FKCG);
                his.setRemark("支付失败！(" + responseMsg + ")\n" +
                        DateUtil.formatDate("yyyy年MM月dd日 HH:mm:ss", order.getLoanEndTime()) +
                        "前未还款，则视为逾期，平台将收取逾期利息。");
            }
        }
        his.setCreateTime(new Date());
        info.setUpdateTime(new Date());//更新时间
        iAssetOrderStatusHisService.saveHis(his);
        iAssetBorrowOrderService.updateByPrimaryKeySelective(order);
        return repaymentInfoDao.updateByPrimaryKeySelective(info);
    }

    @Override
    public void setOrderHisStauts(String repaymentInfoId) {
//        RepaymentInfo info= new RepaymentInfo();
//        info.setId(Integer.parseInt(repaymentInfoId));
//        info=repaymentInfoDao.selectOne(info);
//        AssetOrderStatusHis his=new AssetOrderStatusHis();
//        his.setOrderId(info.getAssetId());
//        his.setOrderStatus(AssetOrderStatusHis.STATUS_DSK);
//        his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_DSK));
//        his.setCreateTime(new Date());
//        iAssetOrderStatusHisService.saveHis(his);
    }

    @Override
    public Map getRepaymentParams(Integer userId, Integer orderId, Integer type, String ip) {
        Map retMap=new HashMap();
        retMap.put("code", "-1");
        BorrowUser user = iBorrowUserService.getBorrowUserById(userId);
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);
        RepaymentInfo info = new RepaymentInfo();
        info.setAssetId(order.getId());
            switch (type) {
                case 1:
                    info.setReqAmt(assetRepaymentOrder.getRepaymentAmount().subtract(assetRepaymentOrder.getRepaymentedAmount()).subtract(assetRepaymentOrder.getCeditAmount()));
                    break;
                case 2:
                    info.setReqAmt(order.getPenaltyAmount());
                    break;
            }
        info.setType(type);
        repaymentInfoDao.insertSelective(info);

        BindCardPayVo vo = new BindCardPayVo();
        vo.setP1_bizType("QuickPayCreateOrder");
        vo.setP2_customerNumber(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MCHNTCD"));
        vo.setP3_userId(order.getUserId() + "");
        vo.setP4_orderId(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") + info.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        vo.setP5_timestamp(sdf.format(new Date()));
        vo.setP6_payerName(order.getUserName());
        vo.setP7_idCardType("IDCARD");//IDCARD：身份证
        vo.setP8_idCardNo(user.getUserCardNo());
        vo.setP9_cardNo(user.getCardCode());
        vo.setP10_year("");
        vo.setP11_month("");
        vo.setP12_cvv2("");
        vo.setP13_phone(user.getCardPhone());
        vo.setP14_currency("CNY");
        vo.setP15_orderAmount(info.getReqAmt().toString());
        vo.setP16_goodsName(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SMS).get("PROJECT_NAME"));
        vo.setP17_goodsDesc("");
        if (order.getDeviceType().equalsIgnoreCase("ios")) {
            vo.setP18_terminalType("UUID");
        } else {
            vo.setP18_terminalType("IMEI");
        }
        vo.setP19_terminalId(order.getDeviceNumber());
        vo.setP20_orderIp(ip);
        vo.setP21_period("1");
        vo.setP22_periodUnit("Hour");
        vo.setP23_serverCallbackUrl(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("HL_BACK_PATH") + "/helipay/callback");
        vo.setP24_isEncrypt("FALSE");

        Map<String, String> map = null;
        try {
            map = MyBeanUtils.convertBean(vo, new LinkedHashMap());
            String[] excludes = {"P17_validateCode","P24_isEncrypt"};
            String oriMessage = MyBeanUtils.getSigned(map, excludes)+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MY_KEY") ;
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            vo.setSign(sign);
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, "http://pay.trx.helipay.com/trx/quickPayApi/interface.action");
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                BindCardPayResponseVo bindCardPayResponseVo = JSONObject.parseObject(resultMsg, BindCardPayResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(bindCardPayResponseVo, null)+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MY_KEY") ;
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = bindCardPayResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(bindCardPayResponseVo.getRt2_retCode())) {
                        retMap.put("code", "0");
                        retMap.put("message", "绑卡交易成功");
                        retMap.put("json", JSONObject.parseObject(resultMsg));
                        retMap.put("amt",info.getReqAmt().toString());
                        retMap.put("userCardCode",user.getCardCode());
                        retMap.put("cardPhone",user.getCardPhone());
                        retMap.put("infoId",info.getId());
                        BankAllInfo bankAllInfo = this.bankAllInfoDao.selectByPrimaryKey(user.getCardType());
                        retMap.put("userCardName",bankAllInfo.getBankName());
                    } else {
                        retMap.put("message", bindCardPayResponseVo.getRt3_retMsg());
                    }
                } else {
                    retMap.put("message", "验签失败，请联系管理员");
                }
            } else {
                retMap.put("message", "请求失败，请联系管理员");
            }
        } catch (Exception e) {
            retMap.put("message", "请求错误，请联系管理员");
        }
        return retMap;
    }

    @Override
    public Map confirmPay(String infoId, String validateCode, String ip) {
        Map retMap=new HashMap();
        retMap.put("code","-1");
        RepaymentInfo info = new RepaymentInfo();
        info.setId(Integer.parseInt(infoId));
        info=repaymentInfoDao.selectOne(info);
        info.setIsSuc(2);
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(info.getAssetId());
        //还款单表
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(order.getId());

        //判断当前订单是否还清、逾期还清
        List<Integer> orderStatuses = Arrays.asList(AssetOrderStatusHis.STATUS_YHK);
        if (assetRepaymentOrder == null)
        {
            log.info("当前订单不存在");
            retMap.put("message", "当前订单异常，请联系客服人员");
            return retMap;
        }
        if (orderStatuses.contains(assetRepaymentOrder.getOrderStatus().intValue()))
        {
            log.info("当前订单已经还清");
            retMap.put("message", "当前订单已经还清");
            return retMap;
        }


        if (info.getType() != null) {
            switch (info.getType()) {
                case 1:
                    if(info.getReqAmt().compareTo(assetRepaymentOrder.getRepaymentAmount().subtract(assetRepaymentOrder.getRepaymentedAmount()))!=0){
                        log.info("当前订单已经状态已经变更\n请退回详情页刷新");
                        retMap.put("message", "当前订单已经状态已经变更\n请退回详情页刷新");
                        return retMap;
                    }
                    break;
                case 2:
                    if(order.getStatus().equals(AssetOrderStatusHis.STATUS_YYQ)){
                        log.info("当前订单已经状态已经变更\n请退回详情页刷新");
                        retMap.put("message", "当前订单已经状态已经变更\n请退回详情页刷新");
                        return retMap;
                    }
                    break;
            }
        }

        //当前还款订单是否处于支付中
        long timeout = RedisUtil.ORDER_PAY_LOCK_TIMEOUT;
        final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + assetRepaymentOrder.getId();
        final String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
        //拿锁 能拿到锁 则可以进行下面操作
        if (!redisUtil.lock(lockKey, time))
        {
            String msg = "订单正在支付处理中";
            Object value = redisUtil.get(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG);
            if (value != null)
            {
                msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
            }
            retMap.put("message", msg);
            log.info("当前订单{},已经在进行还款:{}", lockKey, msg);
            return retMap;
        }
        //设置锁 消息提示
        redisUtil.set(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG, RepaymentLockMsgEnum.QUICK_PAY.getValue(), timeout / 1000);

        AssetOrderStatusHis his = new AssetOrderStatusHis();
        his.setOrderId(order.getId());

        AssetRepaymentOrder tem = iAssetRepaymentOrderService.getRepaymentByOrderId(order.getId());
        ConfirmPayVo vo=new ConfirmPayVo();
        vo.setP1_bizType("QuickPayConfirmPay");
        vo.setP2_customerNumber(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MCHNTCD"));
        vo.setP3_orderId(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") + info.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        vo.setP4_timestamp(sdf.format(new Date()));
        vo.setP5_validateCode(validateCode);
        vo.setP6_orderIp(ip);
        Map<String, String> map = null;
        try {
            map = MyBeanUtils.convertBean(vo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null)+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MY_KEY") ;
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, "http://pay.trx.helipay.com/trx/quickPayApi/interface.action");
            log.info("响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                ConfirmPayResponseVo confirmPayResponseVo = JSONObject.parseObject(resultMsg, ConfirmPayResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(confirmPayResponseVo, null)+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MY_KEY") ;
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = confirmPayResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                info.setOrderId(confirmPayResponseVo.getRt6_serialNumber());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(confirmPayResponseVo.getRt2_retCode())) {
                        retMap.put("code","0");
                        retMap.put("message", "交易成功");
                        retMap.put("json", JSONObject.parseObject(resultMsg));
                    } else {
                        retMap.put("message", confirmPayResponseVo.getRt3_retMsg());
                        //校验还款订单状态
                        RepaymentInfo infoCheck = new RepaymentInfo();
                        infoCheck.setId(info.getId());
                        infoCheck=repaymentInfoDao.selectOne(infoCheck);
                        //如果订单状态为成功则不处理
                        if(infoCheck != null && infoCheck.getIsSuc() != null && infoCheck.getIsSuc() != 1){
                            info.setIsSuc(2);
                            repaymentInfoDao.updateByPrimaryKeySelective(info);
                        }
                    }
                } else {
                    retMap.put("message", "验签失败");
                }
            } else {
                retMap.put("message", "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("message", "交易异常：" + e.getMessage());
        }finally {
            redisUtil.unlock(lockKey);
            return  retMap;
        }
    }

    /**
     * 宝付主动还款
     * @param userId 用户id
     * @param orderId 订单id
     * @param type 还款类型：1-还款，2-续期
     * @param hkType 还款方式：0-主动还款，1-后台代扣
     * @return
     */
    @Override
    public Map baoFooConfirmPay(int userId, int orderId, int type, int hkType) {
        Map retMap=new HashMap();
        retMap.put("code","-1");

        BorrowUser user = iBorrowUserService.getBorrowUserById(userId);
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);

        RepaymentInfo srcInfo = new RepaymentInfo();
        srcInfo.setAssetId(order.getId());
        switch (type) {
            case 1:
                srcInfo.setReqAmt(
                        assetRepaymentOrder.getRepaymentAmount().subtract(
                                assetRepaymentOrder.getRepaymentedAmount()
                        ).subtract(
                                assetRepaymentOrder.getCeditAmount()
                        )
                );
                break;
            case 2:
                srcInfo.setReqAmt(order.getPenaltyAmount());
                break;
        }
        srcInfo.setType(type);
        repaymentInfoDao.insertSelective(srcInfo);

        RepaymentInfo info = new RepaymentInfo();
        info.setId(srcInfo.getId());
        info=repaymentInfoDao.selectOne(info);
        info.setIsSuc(2);
        order = iAssetBorrowOrderService.getOrderById(info.getAssetId());
        //还款单表
        assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(order.getId());

        //判断当前订单是否还清、逾期还清
        List<Integer> orderStatuses = Arrays.asList(AssetOrderStatusHis.STATUS_YHK);
        if (assetRepaymentOrder == null)
        {
            log.info("当前订单不存在");
            retMap.put("message", "当前订单异常，请联系客服人员");
            return retMap;
        }
        if (orderStatuses.contains(assetRepaymentOrder.getOrderStatus().intValue()))
        {
            log.info("当前订单已经还清");
            retMap.put("message", "当前订单已经还清");
            return retMap;
        }

        if (info.getType() != null) {
            switch (info.getType()) {
                case 1:
                    if(info.getReqAmt().compareTo(assetRepaymentOrder.getRepaymentAmount().subtract(assetRepaymentOrder.getRepaymentedAmount()))!=0){
                        log.info("当前订单已经状态已经变更\n请退回详情页刷新");
                        retMap.put("message", "当前订单已经状态已经变更\n请退回详情页刷新");
                        return retMap;
                    }
                    break;
                case 2:
                    if(order.getStatus().equals(AssetOrderStatusHis.STATUS_YYQ)){
                        log.info("当前订单已经状态已经变更\n请退回详情页刷新");
                        retMap.put("message", "当前订单已经状态已经变更\n请退回详情页刷新");
                        return retMap;
                    }
                    break;
            }
        }

        //当前还款订单是否处于支付中
        long timeout = RedisUtil.ORDER_PAY_LOCK_TIMEOUT;
        final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + assetRepaymentOrder.getId();
        final String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
        //拿锁 能拿到锁 则可以进行下面操作
        if (!redisUtil.lock(lockKey, time))
        {
            String msg = "订单正在支付处理中";
            Object value = redisUtil.get(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG);
            if (value != null)
            {
                msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
            }
            retMap.put("message", msg);
            log.info("当前订单{},已经在进行还款:{}", lockKey, msg);
            return retMap;
        }
        //设置锁 消息提示
        redisUtil.set(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG, RepaymentLockMsgEnum.QUICK_PAY.getValue(), timeout / 1000);

        AssetOrderStatusHis his = new AssetOrderStatusHis();
        his.setOrderId(order.getId());

        try {
            String send_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//报文发送日期时间

            String cerpath = PropertiesUtil.get("baofoo.rzzf.pub.key");//宝付公钥
            String pfxpath = PropertiesUtil.get("baofoo.rzzf.pri.key");//商户私钥

            //商户自定义(可随机生成  AES key长度为=16位)
            String AesKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_AES_KEY");
            //使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
            String dgtl_envlp = "01|"+AesKey;
            log.info("----------宝付主动支付----------密码dgtl_envlp："+dgtl_envlp);

            //公钥加密
            dgtl_envlp = RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), cerpath);

            //签约协议号（确认支付返回）
            String ProtocolNo=user.getBindId();
            log.info("----------宝付主动支付----------签约协议号："+ProtocolNo);
            ProtocolNo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(ProtocolNo), AesKey);//先BASE64后进行AES加密
            log.info("----------宝付主动支付----------签约协议号AES结果:"+ProtocolNo);

            String CardInfo="";//信用卡：信用卡有效期|安全码,借记卡：传空

            //暂不支持信用卡
            //CardInfo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(CardInfo), AesKey);//先BASE64后进行AES加密

            //回调地址
            String ReturnUrl=PropertiesUtil.get("local_url")+"/baoFooPay/singlePay/callback";

            //生成订单号，还款类型：0-主动还款，1-后台代扣
            String transId=(hkType==0 ? "HXX_BaoFoo_TID_" : hkType==1 ? "HXX_BaoFoo_DK_TID_" : "") + info.getId();

            Map<String,String> DateArry = new TreeMap<String,String>();
            DateArry.put("send_time", send_time);
            DateArry.put("msg_id", "TISN" + System.currentTimeMillis());//报文流水号
            DateArry.put("version", PropertiesUtil.get("baofoo.rzzf.version"));
            DateArry.put("terminal_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_TERM_ID"));
            DateArry.put("txn_type", "08");//交易类型(参看：文档中《交易类型枚举》)
            DateArry.put("member_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_MER_ID"));
            DateArry.put("trans_id", transId);
            DateArry.put("dgtl_envlp", dgtl_envlp);
            //DateArry.put("user_id", "117300110");//用户在商户平台唯一ID (和绑卡时要一致)
            DateArry.put("protocol_no", ProtocolNo);//签约协议号（密文）
            //交易金额 [单位：分  例：1元则提交100]，此处注意数据类型的转转，建议使用BigDecimal类弄进行转换为字串
            DateArry.put("txn_amt", String.valueOf(info.getReqAmt().multiply(new BigDecimal(100)).intValue()));
            DateArry.put("card_info", CardInfo);//卡信息

            Map<String,String> RiskItem = new HashMap<String,String>();
            /*--------风控基础参数-------------**/
            DateArry.put("risk_item", net.sf.json.JSONObject.fromObject(RiskItem).toString());//放入风控参数

            DateArry.put("return_url", ReturnUrl);//最多填写三个地址,不同地址用间使用‘|’分隔

            String SignVStr = FormatUtil.coverMap2String(DateArry);
            log.info("----------宝付主动支付----------SHA-1摘要字串："+SignVStr);
            String signature = SecurityUtil.sha1X16(SignVStr, "UTF-8");//签名
            log.info("----------宝付主动支付----------SHA-1摘要结果："+signature);
            String Sign = SignatureUtils.encryptByRSA(
                    signature,
                    pfxpath,
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
            );
            log.info("----------宝付主动支付----------RSA签名结果："+Sign);
            DateArry.put("signature", Sign);//签名域

            String PostString  = HttpUtil.RequestForm(
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_URL"),
                    DateArry
            );
            log.info("----------宝付主动支付----------请求返回:"+PostString);

            Map<String, String> ReturnData = FormatUtil.getParm(PostString);

            if(!ReturnData.containsKey("signature")){
                throw new Exception("缺少验签参数！");
            }
            String RSign=ReturnData.get("signature");
            log.info("----------宝付主动支付----------返回的验签值："+RSign);
            ReturnData.remove("signature");//需要删除签名字段
            String RSignVStr = FormatUtil.coverMap2String(ReturnData);
            log.info("----------宝付主动支付----------返回SHA-1摘要字串："+RSignVStr);
            String RSignature = SecurityUtil.sha1X16(RSignVStr, "UTF-8");//签名
            log.info("----------宝付主动支付----------返回SHA-1摘要结果："+RSignature);

            if(SignatureUtils.verifySignature(cerpath,RSignature,RSign)){
                log.info("----------宝付主动支付----------验签Yes");//验签成功
            }
            if(!ReturnData.containsKey("resp_code")){
                throw new Exception("缺少resp_code参数！");
            }
            if(ReturnData.get("resp_code").toString().equals("S")){
                log.info("----------宝付主动支付----------支付成功！");

//                doBaoFooCallback(ReturnData);

                retMap.put("code","0");
                retMap.put("message", ReturnData.get("biz_resp_msg"));
                retMap.put("json", ReturnData);
            }else if(ReturnData.get("resp_code").toString().equals("I")){
                log.info("----------宝付主动支付----------处理中！");
                retMap.put("message", "处理中");
            }else if(ReturnData.get("resp_code").toString().equals("F")){
                log.info("----------宝付主动支付----------失败！");

//                doBaoFooCallback(ReturnData);

                retMap.put("message", ReturnData.get("biz_resp_msg"));

            }else{
                throw new Exception("反回异常！");//异常不得做为订单状态。
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("message", "交易异常：" + e.getMessage());
        }finally {
            redisUtil.unlock(lockKey);
            return  retMap;
        }
    }

    /**
     * 宝付主动还款回调
     * @param params
     * @return
     */
    @Override
    public int doBaoFooCallback(Map<String, String> params) {
        String responseCode = params.get("biz_resp_code");//响应代码
        String responseMsg = params.get("biz_resp_msg");//响应消息
        String repaymentInfoId = params.get("trans_id")
                .substring(params.get("trans_id").lastIndexOf("_")+1);//商户订单号

        String fyOrderId = params.get("trans_id");//宝付订单号
        String amt = params.get("succ_amt");//交易金额
        int isSuc = 2;
        RepaymentInfo info = new RepaymentInfo();
        info.setId(Integer.parseInt(repaymentInfoId));
        info = repaymentInfoDao.selectOne(info);
        if(info.getIsSuc().equals(1)){//如果已经成功了 就直接放回
            return 1;
        }
        //当前还款订单是否处于支付中
        long timeout = 60*1000;
        final String lockKey = RedisUtil.LOCK_PAY_CCALLBACK + repaymentInfoId;
        final String time = System.currentTimeMillis() + 60*1000 + "";// 1分钟
        //拿锁 能拿到锁 则可以进行下面操作
        if (!redisUtil.lock(lockKey, time))
        {
            //订单正在处理中
            return 1;
        }
        //设置锁 消息提示
        redisUtil.set(lockKey, "订单正在处理中", timeout);

        info.setOrderId(fyOrderId);
        info.setResAmt(new BigDecimal(amt).divide(new BigDecimal(100)));
        isSuc = responseCode.equals("0000") ? 1 : 2;
//        isSuc=info.getReqAmt().compareTo(info.getResAmt())==0?1:2;
        info.setIsSuc(isSuc);

        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(info.getAssetId());
        AssetOrderStatusHis his = new AssetOrderStatusHis();
        his.setOrderId(order.getId());

        AssetRepaymentOrder tem = iAssetRepaymentOrderService.getRepaymentByOrderId(order.getId());
        if (isSuc == 1) {
            if (info.getType() != null) {
                switch (info.getType()) {
                    case 1:
                        order.setStatus(AssetOrderStatusHis.STATUS_YHK); //1 审核中 2 待打款 3 待寄出 5 审核拒绝 7 已违约 8 已取消 10 已寄出 11 已完成
                        his.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
//                        if (order.getLateDay() > 0) {
//                            his.setRemark("已取消交易，返还预付款+违约金+滞纳金，共" + info.getResAmt() + "元");
//                        } else {
//                            his.setRemark("已取消交易，返还预付款+违约金，共" + info.getResAmt() + "元");
//                        }
//                        his.setCreateTime(new Date());
//                        iAssetOrderStatusHisService.saveHis(his);

                        his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_YHK));
                        order.setOrderEnd(1);//取消关闭
//                        SmsSendUtil.sendSmsDiy(order.getUserPhone(), SmsContentConstant.CANCEL_ORDER);
                        SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(order.getUserPhone(), SmsContentConstant.CANCEL_ORDER);
                        AssetRepaymentOrder assetRepaymentOrder = new AssetRepaymentOrder();
                        assetRepaymentOrder.setId(tem.getId());
                        assetRepaymentOrder.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
                        assetRepaymentOrder.setUpdateTime(new Date());
                        assetRepaymentOrder.setRepaymentRealTime(new Date());
                        assetRepaymentOrder.setRepaymentType(AssetRepaymentOrder.REPAYMENT_TYPE_INITIATIVE);
                        if(tem.getRepaymentedAmount()==null){
                            assetRepaymentOrder.setRepaymentedAmount(info.getReqAmt());
                        }else{
                            assetRepaymentOrder.setRepaymentedAmount(info.getReqAmt().add(tem.getRepaymentedAmount()));
                        }
                        if(params.get("trans_id").contains("HXX_BaoFoo_DK_TID_")){
                            //修改支付状态为代扣
                            assetRepaymentOrder.setRepaymentType(AssetRepaymentOrder.REPAYMENT_TYPE_DAIKOU);
                            //三方支付成功状态
                            assetRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_SUCCESS);
                            //三方订单号
                            assetRepaymentOrder.setSeqId(info.getOrderId());
                        }
                        iAssetRepaymentOrderService.update(assetRepaymentOrder);

                        BorrowUser user =new BorrowUser();
                        user.setId(tem.getUserId());
                        user.setIsOld(1);
                        iBorrowUserService.updateUser(user);
                        break;
                    case 2:
                        Integer renewalDay = order.getMoneyDay();

                        order.setLoanEndTime(DateUtil.addDay(tem.getRepaymentTime(), renewalDay));

                        his.setOrderStatus(AssetOrderStatusHis.STATUS_YXQ);
                        his.setRemark("已续期，\n" +
                                DateUtil.formatDate("yyyy年MM月dd日 HH:mm:ss", order.getLoanEndTime()) +
                                "前未还款，则视为逾期，平台将收取逾期利息。");
                        AssetRenewalOrder assetRenewalOrder = new AssetRenewalOrder();
                        assetRenewalOrder.setUserId(order.getUserId());
                        assetRenewalOrder.setUserPhone(order.getUserPhone());
                        assetRenewalOrder.setUserName(order.getUserName());
                        assetRenewalOrder.setOrderId(order.getId());
                        assetRenewalOrder.setMoneyAmount(order.getPerPayMoney());
                        assetRenewalOrder.setPenaltyAmount(order.getPenaltyAmount());
                        assetRenewalOrder.setMoneyDay(order.getMoneyDay());
                        assetRenewalOrder.setCreditRepaymentTime(tem.getCreditRepaymentTime());
                        assetRenewalOrder.setRenewalTime(new Date());
                        assetRenewalOrder.setRenewalDay(renewalDay);
                        assetRenewalOrder.setRepaymentTime(DateUtil.addDay(tem.getRepaymentTime(), renewalDay));
                        iAssetRenewalOrderService.insertRenewalOrder(assetRenewalOrder);

                        AssetRepaymentOrder repay = new AssetRepaymentOrder();
                        repay.setId(tem.getId());
                        repay.setUpdateTime(new Date());
                        repay.setOrderRenewal(AssetOrderStatusHis.STATUS_YXQ);
                        repay.setRepaymentTime(DateUtil.addDay(tem.getRepaymentTime(), renewalDay));
                        iAssetRepaymentOrderService.update(repay);

                        break;
                }
            }
        } else {
            //主动还款
            if(params.get("trans_id").contains("HXX_BaoFoo_TID_")){
                if (order.getLateDay() > 0) {
                    //                order.setStatus(AssetOrderStatusHis.STATUS_YYQ);
                    his.setOrderStatus(AssetOrderStatusHis.STATUS_YYQ);
                    his.setRemark("支付失败(" + responseMsg + ")！已逾期");
                } else {
                    //                order.setStatus(AssetOrderStatusHis.STATUS_FKCG);
                    his.setOrderStatus(AssetOrderStatusHis.STATUS_FKCG);
                    his.setRemark("支付失败！(" + responseMsg + ")\n" +
                            DateUtil.formatDate("yyyy年MM月dd日 HH:mm:ss", order.getLoanEndTime()) +
                            "前未还款，则视为逾期，平台将收取逾期利息。");
                }
            }

            //代扣
            if(params.get("trans_id").contains("HXX_BaoFoo_DK_TID_")){
                AssetRepaymentOrder updateRepaymentOrder = new AssetRepaymentOrder();
                updateRepaymentOrder.setId(tem.getId());
                updateRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_FAILED);
                updateRepaymentOrder.setUpdateTime(new Date());
                updateRepaymentOrder.setErrMessage(
                        (tem.getErrMessage() !=null ? tem.getErrMessage() : "")
                                + "||" + tem.getSeqId() + "&" + responseMsg
                );
                iAssetRepaymentOrderService.update(updateRepaymentOrder);
            }
        }
        his.setCreateTime(new Date());
        iAssetOrderStatusHisService.saveHis(his);
        iAssetBorrowOrderService.updateByPrimaryKeySelective(order);
        return repaymentInfoDao.updateByPrimaryKeySelective(info);
    }

    /**
     * 查询支付订单状态
     * @param transId
     * @return
     * @throws Exception
     */
    @Override
    public Map queryBaoFooPayOrder(String transId) throws Exception {
        String send_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//报文发送日期时间

        String cerpath = PropertiesUtil.get("baofoo.rzzf.pub.key");//宝付公钥
        String pfxpath = PropertiesUtil.get("baofoo.rzzf.pri.key");//商户私钥

        Map<String,String> DateArry = new TreeMap<>();
        DateArry.put("send_time", send_time);
        DateArry.put("msg_id", "HXX_TISN_"+System.currentTimeMillis());//报文流水号
        DateArry.put("version", PropertiesUtil.get("baofoo.rzzf.version"));
        DateArry.put("terminal_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_TERM_ID"));
        DateArry.put("txn_type", "07");//交易类型
        DateArry.put("member_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_MER_ID"));
        DateArry.put("orig_trans_id", transId);//交易时的trans_id
        DateArry.put("orig_trade_date", send_time);//

        String SignVStr = FormatUtil.coverMap2String(DateArry);
        log.info("SHA-1摘要字串："+SignVStr);
        String signature = SecurityUtil.sha1X16(SignVStr, "UTF-8");//签名
        log.info("SHA-1摘要结果："+signature);
        String Sign = SignatureUtils.encryptByRSA(
                signature,
                pfxpath,
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
        );
        log.info("RSA签名结果："+Sign);
        DateArry.put("signature", Sign);//签名域

        String PostString  = HttpUtil.RequestForm(
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_URL"),
                DateArry
        );
        log.info("请求返回:"+PostString);

        Map<String, String> ReturnData = FormatUtil.getParm(PostString);

        if(!ReturnData.containsKey("signature")){
            throw new Exception("缺少验签参数！");
        }

        String RSign=ReturnData.get("signature");
        log.info("返回的验签值："+RSign);
        ReturnData.remove("signature");//需要删除签名字段
        String RSignVStr = FormatUtil.coverMap2String(ReturnData);
        log.info("返回SHA-1摘要字串："+RSignVStr);
        String RSignature = SecurityUtil.sha1X16(RSignVStr, "UTF-8");//签名
        log.info("返回SHA-1摘要结果："+RSignature);

        if(SignatureUtils.verifySignature(cerpath,RSignature,RSign)){
            log.info("Yes");//验签成功
        }
        if(!ReturnData.containsKey("resp_code")){
            throw new Exception("缺少resp_code参数！");
        }
        if(ReturnData.get("resp_code").toString().equals("S")){
            log.info("交易成功！");
        }else if(ReturnData.get("resp_code").toString().equals("I")){
            log.info("处理中！");
        }else if(ReturnData.get("resp_code").toString().equals("F")){
            log.info("失败！");
        }else{
            throw new Exception("反回异常！");//异常不得做为订单状态。
        }
        return ReturnData;
    }
}
