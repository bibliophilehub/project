package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.constants.SmsContentConstant;
import com.inext.dao.HcRepaymentInfoDao;
import com.inext.entity.*;
import com.inext.enums.RepaymentLockMsgEnum;
import com.inext.service.*;
import com.inext.utils.DateUtil;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.huichaopay.TestUtil;
import com.inext.utils.sms.SmsSendUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * huichao
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class HcRepaymentServiceImpl implements IHcRepaymentService {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HcRepaymentServiceImpl.class);
    @Resource
    IBorrowUserService iBorrowUserService;
    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    HcRepaymentInfoDao hcRepaymentInfoDao;
    @Resource
    IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;
    @Resource
    IAssetRenewalOrderService iAssetRenewalOrderService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 回调处理
     * @param params
     * @return
     */
    @Override
    public int doCcallback(Map<String, String> params) {
        String responseCode = params.get("payResult");//响应代码
        String responseMsg = params.get("msg");//响应消息
        String hcRepaymentInfoId = params.get("merchantOutOrderNo")
                .substring(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("ID_KEY").length());//商户订单号

        String hcOrderId = params.get("orderNo");//汇潮订单号
        String amt = JSONObject.parseObject(params.get("msg")).getString("payMoney");//交易金额
        int isSuc = 2;
        HcRepaymentInfo info = new HcRepaymentInfo();
        info.setId(Integer.parseInt(hcRepaymentInfoId));
        info = hcRepaymentInfoDao.selectOne(info);
        if(info.getIsSuc().equals(1)){//如果已经成功了 就直接放回
            return 1;
        }
        //当前还款订单是否处于支付中
        long timeout = 60*1000;
        final String lockKey = RedisUtil.LOCK_PAY_CCALLBACK +"huichao_"+ hcRepaymentInfoId;
        final String time = System.currentTimeMillis() + 60*1000 + "";// 1分钟
        //拿锁 能拿到锁 则可以进行下面操作
        if (!redisUtil.lock(lockKey, time))
        {
            //订单正在处理中
            return 1;
        }
        //设置锁 消息提示
        redisUtil.set(lockKey, "订单正在处理中", timeout);

        info.setOrderId(params.get("merchantOutOrderNo"));
        info.setResAmt(new BigDecimal(amt));
        isSuc = responseCode.equals("1") ? 1 : 2;
//        isSuc=info.getReqAmt().compareTo(info.getResAmt())==0?1:2;
        info.setIsSuc(isSuc);
        info.setAliNo(params.get("aliNo"));

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
//                            his.setRemark("已还款，预付款+违约金+滞纳金，共" + info.getResAmt() + "元");
//                        } else {
//                            his.setRemark("已还款，预付款+违约金，共" + info.getResAmt() + "元");
//                        }
//                        his.setCreateTime(new Date());
//                        iAssetOrderStatusHisService.saveHis(his);

                        his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_YHK));
                        order.setOrderEnd(1);//取消关闭
                        //SmsSendUtil.sendSmsDiy(order.getUserPhone(), SmsContentConstant.CANCEL_ORDER);
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
        info.setUpdateTime(new Date());
        iAssetOrderStatusHisService.saveHis(his);
        iAssetBorrowOrderService.updateByPrimaryKeySelective(order);
        return hcRepaymentInfoDao.updateByPrimaryKeySelective(info);
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

    /**
     * 获取请求参数
     * @param userId 用户id
     * @param orderId 订单id
     * @param type 还款类型
     * @return
     */
    @Override
    public Map<String,Object> getRepaymentParams(Integer userId, Integer orderId, Integer type, String deviceType) {
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("code", "-1");
        //判断当前订单是否还清、逾期还清
        List<Integer> orderStatuses = Arrays.asList(AssetOrderStatusHis.STATUS_YHK);
        //BorrowUser user = iBorrowUserService.getBorrowUserById(userId);
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);

        if(order==null || assetRepaymentOrder == null){
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

        HcRepaymentInfo info = new HcRepaymentInfo();
        info.setAssetId(order.getId());
            switch (type) {
                case 1:
                    info.setReqAmt(
                            assetRepaymentOrder.getRepaymentAmount()
                                    .subtract(assetRepaymentOrder.getRepaymentedAmount())
                                    .subtract(assetRepaymentOrder.getCeditAmount())
                    );
                    break;
                case 2:
                    info.setReqAmt(order.getPenaltyAmount());
                    break;
            }
        info.setType(type);
        hcRepaymentInfoDao.insertSelective(info);
        //查询记录
        HcRepaymentInfo hcRepaymentInfo = new HcRepaymentInfo();
        hcRepaymentInfo.setId(info.getId());
        hcRepaymentInfo=hcRepaymentInfoDao.selectOne(hcRepaymentInfo);

        if (hcRepaymentInfo.getType() != null) {
            switch (hcRepaymentInfo.getType()) {
                case 1:
                    if(hcRepaymentInfo.getReqAmt()
                            .compareTo(assetRepaymentOrder.getRepaymentAmount()
                                    .subtract(assetRepaymentOrder.getRepaymentedAmount()))!=0){
                        log.info("当前订单状态已经变更\n请退回详情页刷新");
                        retMap.put("message", "当前订单状态已经变更\n请退回详情页刷新");
                        return retMap;
                    }
                    break;
                case 2:
                    if(order.getStatus().equals(AssetOrderStatusHis.STATUS_YYQ)){
                        log.info("当前订单状态已经变更\n请退回详情页刷新");
                        retMap.put("message", "当前订单状态已经变更\n请退回详情页刷新");
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
        try{
            Map<String, String> paraMap = new HashMap<>();
            String sign = "";// 签名
            String id = "";
            paraMap.put("merchantOutOrderNo", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("ID_KEY") + info.getId());
            paraMap.put("merid", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("MERID"));
            paraMap.put("noncestr", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("NONCESTR"));
            paraMap.put("orderMoney", info.getReqAmt().toString());
            paraMap.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            paraMap.put("notifyUrl", PropertiesUtil.get("local_url")+"/huichaopay/callback");
            /*
             * 对参数按照 key=value 的格式，并参照参数名 ASCII 码排序后得到字符串 stringA
             */
            String stringA = TestUtil.formatUrlMap(paraMap, false, false);
            System.out.println("stringA:" + stringA);
            /*
             * 在 stringA 最后拼接上 key 得到 stringsignTemp 字符串， 并对 stringsignTemp 进行 MD5 运算，得到
             * sign 值
             */
            String stringsignTemp =
                    stringA + "&key=" + Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("HUICHAO_KEY");
            sign = TestUtil.getMD5(stringsignTemp);
            System.out.println("sign:" + sign);

            /*
             * 对参数按照 key=value 的格式,参照参数名 ASCII 码排序,并对value做utf-8的encode编码后得到字符串 param
             */
            String param = TestUtil.formatUrlMap(paraMap, true, false);

            //将此URL送至APP前端页面或手机浏览器打开，即可自动调起支付宝(需要安装)发起支付
            String url = URLEncoder.encode(
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("ALI_PAY_URL")
                    + "?" + param + "&sign=" + sign + "&id=" + id,
                    "UTF-8");
            if("1".equals(deviceType)){
                //安卓处理
                url = "alipays://platformapi/startApp?appId=10000011&url=" + url;
            }
            if("2".equals(deviceType)){
                //ios处理
                url = "alipay://platformapi/startApp?appId=10000011&url=" + url;
            }
            System.out.println("url:" + url);
            retMap.put("code","0");
            retMap.put("message","操作成功");
            retMap.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("message", "交易异常：" + e.getMessage());
        }finally {
            redisUtil.unlock(lockKey);
        }
        return retMap;
    }

    @Override
    public Map<String, Object> queryHuiChaoOrder(Integer orderId) {
        Map<String, Object> retMap=new HashMap<>();
        if(orderId == null){
            retMap.put("code", "-1");
            retMap.put("message", "查询失败,订单id不能为空");
            return retMap;
        }
        HcRepaymentInfo hcRepaymentInfo= iAssetRepaymentOrderService.getOneByHcRepaymentOrder(orderId);
        if(hcRepaymentInfo == null || hcRepaymentInfo.getId() == null){
            retMap.put("code", "-1");
            retMap.put("message", "查询失败,未查询到记录！");
            return retMap;
        }
        //拼装参数
        Map<String, String> param=new HashMap<>();
        param.put("merchantOutOrderNo",
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("ID_KEY") + hcRepaymentInfo.getId());
        param.put("merid", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("MERID"));
        param.put("noncestr", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("NONCESTR"));
        //将参数转换为key=value形式
        String paramStr=TestUtil.formatUrlMap(param, false, false);
        //在最后拼接上密钥
        String signStr=paramStr+"&key="+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("HUICHAO_KEY");
        //MD5签名
        String sign=TestUtil.getMD5(signStr);
        //拼接签名
        paramStr=paramStr+"&sign="+sign;
        //发起查询
        String result=TestUtil.sendPost(
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("QUERY_URL"),
                paramStr,
                "UTF-8"
        );
        System.out.print("查询结果:"+result);
        //查询结果为json字符串，转换为json对象
        JSONObject resJson=JSONObject.parseObject(result);
        //订单金额
        String orderMoney=resJson.getString("orderMoney");
        //支付结果
        Integer payResult=resJson.getInteger("payResult");
        //平台订单号
        String orderNo=resJson.getString("orderNo");

        String code=resJson.getString("code");
        String message=resJson.getString("message");
        if(StringUtils.isNotEmpty(code)) {
            //若code存在，则
            retMap.put("code", "-1");
            retMap.put("message", "查询失败" + message);
            return retMap;
        }
        retMap.put("code", "0");
        retMap.put("data",resJson);
        return retMap;
    }
}
