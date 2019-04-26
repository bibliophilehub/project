package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.inext.constants.Constants;
import com.inext.constants.SmsContentConstant;
import com.inext.dao.*;
import com.inext.entity.*;
import com.inext.enumerate.ApiStatus;
import com.inext.enumerate.Status;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.*;
import com.inext.service.pay.IPaymentService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import com.inext.utils.sms.SmsSendUtil;
import com.inext.view.params.PagingParams;
import com.inext.view.result.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("assetBorrowOrderService")
public class AssetBorrowOrderServiceImpl implements IAssetBorrowOrderService {

    public static final Logger logger = LoggerFactory.getLogger(AssetBorrowOrderServiceImpl.class);
    @Resource
    IAssetBorrowOrderDao iAssetBorrowOrderDao;
    @Resource
    IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Resource
    IEquipmentDao iEquipmentDao;
    @Resource
    IBorrowUserService iBorrowUserService;
    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;

    @Resource
    IRiskCreditUserDao iRiskCreditUserDao;

    @Autowired
    IBankAllInfoDao bankAllInfoDao;

    @Autowired
    IAssetRepaymentOrderDao iAssetRepaymentOrderDao;
    @Resource
    private IAssetOrderStatusHisDao iAssetOrderStatusHisDao;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    
    @Autowired
    private IBorrowUserDao userDao;
    
    @Autowired
    private IChannelDao channelDao;

    /**
     * 订单支付类
     */
    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IRiskCreditUserService iRiskCreditUserService;

    @Override
    public List<AssetBorrowOrder> getOrderList(Map<String, Object> param) {

        List<AssetBorrowOrder> orderList = iAssetBorrowOrderDao.getOrderList(param);
        if (null != orderList && orderList.size() > 0) {
            for (AssetBorrowOrder o : orderList) {
                AssetOrderStatusHis his = iAssetOrderStatusHisService.getLastOrderHis(o.getId());
                if (null != his) {
                    o.setStatus(his.getOrderStatus());
                    o.setHisTime(his.getCreateTime());
                }
            }
        }
        return orderList;
    }


    @Override
    public PageInfo<AssetBorrowOrder> getOrderPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }

        List<AssetBorrowOrder> list = new ArrayList<>();
        if(params.containsKey("status") && params.get("status").equals("4")){
            list = this.iAssetBorrowOrderDao.getOrderListByFail(params);
        }else{
            list = this.iAssetBorrowOrderDao.getOrderList(params);
        }
        for( AssetBorrowOrder item : list ){
            RiskCreditUser riskCreditUser = iRiskCreditUserService.getByAssetId(item.getId().toString());
            if( riskCreditUser == null ){
                item.setScore("未风控");
                item.setDetail("");
                continue;
            }
            item.setScore(riskCreditUser.getScore().toString());
            item.setDetail(riskCreditUser.getDetail());
            
            BorrowUser user = userDao.getBorrowUserByUId(item.getUserId());
            ChannelInfo channel = channelDao.getChannelById(user.getChannelId());
            if(channel==null){
            	item.setChannelName("自然流量");
            }else{
            	item.setChannelName(channel.getChannelName());
            }
            
            
        }
        PageInfo<AssetBorrowOrder> pageInfo = new PageInfo<AssetBorrowOrder>(list);
        return pageInfo;
    }


    @Override
    public AssetBorrowOrder getOrderById(Integer id) {
        return iAssetBorrowOrderDao.getOrderById(id);
    }

    /**
     * 检查当前用户是否存在未还款完成的订单
     *
     * @param userId
     * @return 1：是；0：否
     */
    @Override
    public Integer checkBorrow(Integer userId, String deviceNumber) {
        Integer result = 0;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("orderEnd", "0");
        params.put("deviceNumber", deviceNumber);
        List<AssetBorrowOrder> list = iAssetBorrowOrderDao.findParams(params);

        if (list != null && list.size() > 0) {
            result = 1;
        }
        return result;
    }

    @Override
    public Map<String, String> findAuditFailureOrderByUserId(Integer userId) {
        Map<String, String> result = new HashMap<String, String>();
        Integer code = 0;
        String msg = "";
        int nextLoanDay = 0;// 剩余可借款天数
        Integer interval_day = 0; // 申请失败距当前时间的间隔天数
        Map<String, String> map2 = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.INTERVAL);
        int interval = Integer.valueOf(map2.get("INTERVAL_BORROW_AGAIN"));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("status", AssetOrderStatusHis.STATUS_SHJJ);
        List<AssetBorrowOrder> list = iAssetBorrowOrderDao.getOrderList(params);

        AssetBorrowOrder bo;
        if (list != null && list.size() > 0) {
            bo = list.get(0);
            Date date = new Date();
            Date smDate = new Date();
            try {
                interval_day = DateUtil.daysBetween(bo.getAuditTime(), date);
                code = interval_day < interval ? -1 : 0;
            } catch (Exception e) {
                code = -1;
            }
            if (code == -1) {
                msg = "距离上次审核失败不足" + interval + "天，请" + (interval - interval_day) + "天后重新申请。";
                nextLoanDay = (interval - interval_day);
                result.put("canLoan", new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.addDay(smDate, interval - 1)));
            }

        }
        result.put("code", code + "");
        result.put("msg", msg);
        result.put("nextLoanDay", String.valueOf(nextLoanDay));
        return result;
    }


    @Override
    public Map<String, Object> saveLoan(Map<String, String> params, BorrowUser bu) {

        Map<String, Object> result = new HashMap<String, Object>();

        if (bu.getIsCard().intValue() == 0 || StringUtils.isBlank(bu.getCardCode())) {
            result.put("code", Status.FAIL.getName());
            result.put("msg", "请先绑定银行卡");
            return result;
        }


        Map<String, String> map2 = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SYS_FEE);
        Integer lateApr = Integer.valueOf(map2.get("fee_lateapr"));

//都是整数 不需要*10
        Integer perPayMoney = Integer.parseInt(params.get("perPayMoney")); //预付款
        Integer penaltyAmount = Integer.parseInt(params.get("penaltyAmount")); //违约金
        Integer moneyAmount = Integer.parseInt(params.get("moneyAmount")); //应退还总额
//        Integer perPayMoney = Integer.parseInt(params.get("perPayMoney")) * 100; //预付款
//        Integer penaltyAmount = Integer.parseInt(params.get("penaltyAmount")) * 100; //违约金
//        Integer moneyAmount = Integer.parseInt(params.get("moneyAmount")) * 100; //应退还总额
        if (perPayMoney.intValue() + penaltyAmount.intValue() != moneyAmount.intValue()) {
            result.put("code", Status.FAIL.getName());
            result.put("msg", "金额错误");
            return result;
        }
        String deviceType = params.get("deviceType");
        deviceType = StringUtils.isNotEmpty(deviceType) ? deviceType : "h5";
        Date date = new Date();
        AssetBorrowOrder order = new AssetBorrowOrder();

        Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
        perPayMoney = Integer.parseInt(loan.get("LOAN_PER"));//预付款
        penaltyAmount = Integer.parseInt(loan.get("LOAN_WY"));//违约金
        moneyAmount = Integer.parseInt(loan.get("LOAN_ALL"));//应退还总额
        //查询用户是否设置渠道借款额度
        ChannelLoanQuota channelLoanQuota = channelDao.getChannelLoanQuotaByChannelId(bu.getChannelId());
        if(channelLoanQuota != null
                && StringUtils.isNotEmpty(channelLoanQuota.getLoanPer())
                && StringUtils.isNotEmpty(channelLoanQuota.getLoanWy())
                && StringUtils.isNotEmpty(channelLoanQuota.getLoanAll())
                && NumberUtils.toInt(channelLoanQuota.getLoanPer()) > 0
                && NumberUtils.toInt(channelLoanQuota.getLoanWy()) > 0
                && NumberUtils.toInt(channelLoanQuota.getLoanAll()) > 0
        ){
            //取渠道设置的借款额度
            perPayMoney=NumberUtils.toInt(channelLoanQuota.getLoanPer());
            penaltyAmount = NumberUtils.toInt(channelLoanQuota.getLoanWy());
            moneyAmount = NumberUtils.toInt(channelLoanQuota.getLoanAll());
        }

        //老用户提额
        if(bu.getIsOld() == 1) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", bu.getId());
            param.put("intervalDays", Integer.parseInt(loan.get("LOAN_OLD_INTERVAL_DAY"))); //实际付款与放款时间的间隔天数
            int effectCount = iAssetRepaymentOrderDao.getEffectCount(param); //提额有效的还款记录数
            if(effectCount > 0) {
                int oldAdd = Integer.parseInt(loan.get("LOAN_OLD_ADD")); //老用户提额金额
                int wyPoint = Integer.parseInt(loan.get("LOAN_OLD_WY_POINT")); //老用户违约点数
                int oldMax = Integer.parseInt(loan.get("LOAN_OLD_MAX")); //老用户提额金额
                moneyAmount = moneyAmount + oldAdd * effectCount;
                if(moneyAmount > oldMax) {
                    moneyAmount = oldMax;
                }
                penaltyAmount = moneyAmount * wyPoint / 100;
                perPayMoney = moneyAmount - penaltyAmount;
            }
        }

        order.setUserId(bu.getId());
        order.setUserPhone(bu.getUserPhone());
        order.setUserName(bu.getUserName());
        order.setUserIdNumber(bu.getUserCardNo());
        order.setMoneyAmount(new BigDecimal(moneyAmount));
        order.setPerPayMoney(new BigDecimal(perPayMoney));
        order.setMoneyDay(Integer.parseInt(params.get("moneyDay")));
        order.setPenaltyAmount(new BigDecimal(penaltyAmount));
        order.setOrderTime(date);
        order.setLateFeeApr(new BigDecimal(lateApr));
        order.setUserCardCode(bu.getCardCode());
        order.setUserCardType(com.inext.utils.StringUtils.toString(bu.getCardType()));
        order.setStatus(1);
        order.setDeviceNumber(params.get("deviceNumber"));
        order.setDeviceModel(params.get("deviceModel"));
        order.setDeviceType(deviceType);
        order.setOrderEnd(0);
        order.setUpdateTime(date);
        order.setApplyNew(bu.getIsOld().equals(1)?1:0);
        iAssetBorrowOrderDao.insertSelective(order);


        AssetOrderStatusHis his = new AssetOrderStatusHis();
        his.setOrderId(order.getId());
        his.setOrderStatus(AssetOrderStatusHis.STATUS_DSH);
        his.setRemark("订单已提交");
        his.setCreateTime(date);
        iAssetOrderStatusHisService.saveHis(his);

        //创建征信风控记录
        RiskCreditUser user = new RiskCreditUser();
        user.setUserId(order.getUserId());
        user.setAssetId(order.getId());
        user.setIp(params.get("ip"));
        user.setStatus(1);//2018-08-30 下架除聚信力外的征信
        iRiskCreditUserDao.insertUser(user);

//        result.put("code", Status.SUCCESS.getName());
        result.put("code", "0");
        result.put("msg", Status.SUCCESS.getValue());
        result.put("result", order.getId());
        return result;
    }


    @Override
    public int updateByPrimaryKeySelective(AssetBorrowOrder assetBorrowOrder) {

        return iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);
    }

    @Override
    public ApiServiceResult<PagingResult<AssetBorrowOrderResult>> getOrderList(PagingParams pagingParams, Map<String, Object> param) {

        PageHelper.startPage(pagingParams.getPageNum(), pagingParams.getPageSize());
        List<AssetBorrowOrder> orderList = this.getOrderList(param);
        PageInfo pageInfo = new PageInfo(orderList);

        List<AssetBorrowOrderResult> list = Lists.newArrayList();
        for (AssetBorrowOrder order : orderList) {
            AssetBorrowOrderResult assetBorrowOrderResult = new AssetBorrowOrderResult();
            assetBorrowOrderResult.setId(order.getId());
            assetBorrowOrderResult.setDeviceModel(order.getDeviceModel());
            assetBorrowOrderResult.setDateTime(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getOrderTime()));
            assetBorrowOrderResult.setStatus(order.getStatus());
            assetBorrowOrderResult.setStatusStr(AssetOrderStatusHis.orderStatusMap.get(order.getStatus()));
            assetBorrowOrderResult.setPerPayMoney(order.getPerPayMoney().toString());
            assetBorrowOrderResult.setPenaltyAmount(order.getPenaltyAmount().toString());
            list.add(assetBorrowOrderResult);
        }

        PagingResult pagingResult = new PagingResult(pageInfo, list);
        return new ApiServiceResult<>(pagingResult);
    }

    @Override
    public ApiServiceResult<AssetBorrowOrderDetailsResult> getById(Integer id) {
        AssetBorrowOrder order = this.getOrderById(id);
        AssetBorrowOrderDetailsResult detailsResult = new AssetBorrowOrderDetailsResult();
        AssetOrderStatusHis lastOrderHis = iAssetOrderStatusHisService.getLastOrderHis(order.getId());
        //查询延期还款状态
        Map<String, String> map = backConfigParamsService.getBackConfig(BackConfigParams.POSTPONE, null);
        String sysKey= map.get("LIMIT_SWITCH");
        detailsResult.setDelaySwitch(sysKey);
        //0:未打开,1打开
        detailsResult.setId(order.getId());
        detailsResult.setDeviceModel(order.getDeviceModel());
        detailsResult.setPerPayMoney(order.getPerPayMoney().toString());
        if (null != lastOrderHis) {
            detailsResult.setStatus(lastOrderHis.getOrderStatus());
            //detailsResult.setStatus(order.getStatus());
            detailsResult.setStatusStr(AssetOrderStatusHis.orderStatusMap.get(lastOrderHis.getOrderStatus()));
        }
        detailsResult.setPenaltyAmount(order.getPenaltyAmount().toString());
        detailsResult.setUserCardCode(order.getUserCardCode());
        detailsResult.setUserCardType(bankAllInfoDao.selectByPrimaryKey(Integer.parseInt(order.getUserCardType())).getBankName());
        detailsResult.setDateTime(DateUtils.parseToDateTimeStr(order.getOrderTime()));
        detailsResult.setLateDay(order.getLateDay().toString());
        detailsResult.setLoanEndTime(DateUtils.parseToDateTimeStr(order.getLoanEndTime()));
        detailsResult.setPayEndTime(DateUtils.parseToDateTimeStr(order.getLoanEndTime()));
        detailsResult.setLateMoney(order.getLateMoney() + "");

        List<AssetOrderStatusHis> hisList = iAssetOrderStatusHisService.getOrderHisListByOrderId(order.getId());

        List<AssetBorrowOrderStatusHistoryResult> history = Lists.newArrayList();
        for (AssetOrderStatusHis his : hisList) {
            AssetBorrowOrderStatusHistoryResult historyResult = new AssetBorrowOrderStatusHistoryResult();
            historyResult.setDateTime(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", his.getCreateTime()));
            historyResult.setStatusStr(his.getRemark());
            history.add(historyResult);
        }

        detailsResult.setHistoryList(history);
        return new ApiServiceResult<>(detailsResult);
    }

    @Override
    public ApiServiceResult<OrderBeforeResult> getOrderBefore(String equipmentId, BorrowUser bu) {
        bu = iBorrowUserService.getBorrowUserById(bu.getId());
        EquipmentInfo equipmentInfo = iEquipmentDao.getDataById(equipmentId);
        OrderBeforeResult result = new OrderBeforeResult();

        Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
        String perPayMoney = loan.get("LOAN_PER");//预付款
        String penaltyAmount = loan.get("LOAN_WY");//违约金
        String moneyAmount = loan.get("LOAN_ALL");//应退还总额
        String moneyDay = loan.get("LOAN_DAY");
        //查询用户是否设置渠道借款额度
        ChannelLoanQuota channelLoanQuota = channelDao.getChannelLoanQuotaByChannelId(bu.getChannelId());
        if(channelLoanQuota != null
                && StringUtils.isNotEmpty(channelLoanQuota.getLoanPer())
                && StringUtils.isNotEmpty(channelLoanQuota.getLoanWy())
                && StringUtils.isNotEmpty(channelLoanQuota.getLoanAll())
                && NumberUtils.toInt(channelLoanQuota.getLoanPer()) > 0
                && NumberUtils.toInt(channelLoanQuota.getLoanWy()) > 0
                && NumberUtils.toInt(channelLoanQuota.getLoanAll()) > 0
        ){
            //取渠道设置的借款额度
            perPayMoney=channelLoanQuota.getLoanPer();
            penaltyAmount = channelLoanQuota.getLoanWy();
            moneyAmount = channelLoanQuota.getLoanAll();
        }

        //老用户提额
        if(bu.getIsOld() == 1) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", bu.getId());
            param.put("intervalDays", Integer.parseInt(loan.get("LOAN_OLD_INTERVAL_DAY"))); //实际付款与放款时间的间隔天数
            int effectCount = iAssetRepaymentOrderDao.getEffectCount(param); //提额有效的还款记录数
            if(effectCount > 0) {
                int oldAdd = Integer.parseInt(loan.get("LOAN_OLD_ADD")); //老用户提额金额
                int wyPoint = Integer.parseInt(loan.get("LOAN_OLD_WY_POINT")); //老用户违约点数
                int oldMax = Integer.parseInt(loan.get("LOAN_OLD_MAX")); //老用户提额金额
                int moneyAmountValue = Integer.valueOf(moneyAmount) + oldAdd * effectCount;
                if(moneyAmountValue > oldMax) {
                    moneyAmountValue = oldMax;
                }
                int penaltyAmountValue = moneyAmountValue * wyPoint / 100;
                int perPayMoneyValue = moneyAmountValue - penaltyAmountValue;

                perPayMoney = "" + perPayMoneyValue;
                penaltyAmount = "" + penaltyAmountValue;
                moneyAmount = "" + moneyAmountValue;
            }
        }

// 原先直接取手机型号的价格
//        result.setPerPayMoney(equipmentInfo.getPrice().toString());
//        result.setPenaltyAmount(EquipmentInfo.WY_PRICE);
//        result.setPerformanceDay(EquipmentInfo.LY_DAY);

        result.setEquipmentName(equipmentInfo.getEquipmentName());
        result.setPerPayMoney(perPayMoney);
        result.setPenaltyAmount(penaltyAmount);
        result.setPerformanceDay(moneyDay);
        result.setUserCardCode(bu.getCardCode());

        BankAllInfo bankAllInfo = this.bankAllInfoDao.selectByPrimaryKey(bu.getCardType());
        result.setUserCardName(bankAllInfo.getBankName());

        return new ApiServiceResult<>(result);
    }

    @Override
    public List<AssetBorrowOrder> findAll(HashMap<String, Object> params) {
        return iAssetBorrowOrderDao.findParams(params);
    }


    @Override
    public ApiServiceResult payForAnother(List<AssetBorrowOrder> assetBorrowOrders) throws Exception {
        for (AssetBorrowOrder assetBorrowOrder : assetBorrowOrders) {
            // 生成订单号
            assetBorrowOrder.setNoOrder(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") + assetBorrowOrder.getId());

            ApiServiceResult<PaymentChannelEnum> apiServiceResult = paymentService.paymentOrder(assetBorrowOrder);
            logger.info("订单ID:{} 处理结果:{}", assetBorrowOrder.getId(), com.alibaba.fastjson.JSONObject.toJSONString(apiServiceResult));
            if (apiServiceResult.isSuccessed()) {
                assetBorrowOrder.setStatus(AssetBorrowOrder.STATUS_FKZ);
                assetBorrowOrder.setPayStatus(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                // 设置订单的支付通道 确定该笔订单到底是由哪个通道支付的
                PaymentChannelEnum paymentChannelEnum = apiServiceResult.getExt();
                assetBorrowOrder.setPaymentChannel(paymentChannelEnum.getCode());
            } else {
                assetBorrowOrder.setStatus(AssetOrderStatusHis.STATUS_DFK);
                assetBorrowOrder.setPayStatus(AssetBorrowOrder.SUB_PAY_CSZT);
            }

            assetBorrowOrder.setUpdateTime(new Date());
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);
        }


        return new ApiServiceResult();
    }

    /**
     * 处理代付结果
     *
     * @param apiServiceResult 代付是否成功的参数
     * @param assetBorrowOrder
     * @return
     */
    public ApiServiceResult handlePayForAnotherResult(ApiServiceResult apiServiceResult, AssetBorrowOrder assetBorrowOrder) {

        // 因为使用的是通用的update 方法  所以使用新的类当参数  拒绝参数污染
        AssetBorrowOrder updateAssetBorrowOrder = new AssetBorrowOrder();
        // 代付的订单已经成功 更新订单信息和订单状态历史记录并生成还款信息
        // 其它处理中的状态可忽视
        if (apiServiceResult.isSuccessed()) {
            AssetRepaymentOrder assetRepaymentOrder = new AssetRepaymentOrder();
            assetRepaymentOrder.setUserId(assetBorrowOrder.getUserId());
            assetRepaymentOrder.setUserPhone(assetBorrowOrder.getUserPhone());
            assetRepaymentOrder.setUserName(assetBorrowOrder.getUserName());
            assetRepaymentOrder.setOrderId(assetBorrowOrder.getId());
            //本金
            assetRepaymentOrder.setMoneyAmount(assetBorrowOrder.getPerPayMoney());
            //违约金
            assetRepaymentOrder.setPenaltyAmount(assetBorrowOrder.getPenaltyAmount());
            //总还款金额=本金+违约金
            assetRepaymentOrder.setRepaymentAmount(assetRepaymentOrder.getMoneyAmount().add(assetRepaymentOrder.getPenaltyAmount()));
            //滞纳金利率
            assetRepaymentOrder.setLateFeeApr(assetBorrowOrder.getLateFeeApr());
            //'放款时间'
            assetRepaymentOrder.setCreditRepaymentTime(new Date());
            //'应还款时间'
            Date nowDate = null;
            try {
                nowDate = DateUtils.convertStringToDate(DateUtils.formatDate(new Date(), DateUtils.DEFAULT_FORMAT));
                // 设为23:59:59
                nowDate = DateUtil.addHour(DateUtil.addMinute(DateUtil.addSecond(nowDate, 59), 59), 23);
            } catch (ParseException e) {
                logger.info("时间转换异常 订单id：{}", assetBorrowOrder.getId());
            }
            //6待还款
            // 更新放款时间
            AssetBorrowOrder assetBorrowOrderNew = new AssetBorrowOrder();
            assetBorrowOrderNew.setId(assetBorrowOrder.getId());
            /**
             * 放款时间
             */
            assetBorrowOrderNew.setLoanTime(new Date());
            assetBorrowOrderNew.setLoanEndTime(assetRepaymentOrder.getRepaymentTime());
            assetBorrowOrderNew.setPayRemark("代付成功");
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrderNew);
            assetRepaymentOrder.setRepaymentTime(DateUtil.addDay(nowDate, assetBorrowOrder.getMoneyDay().intValue() - 1));
            assetRepaymentOrder.setUpdateTime(new Date());
            //6待还款
            assetRepaymentOrder.setOrderStatus(AssetRepaymentOrder.STATUS_DHK);
            int i1 = iAssetRepaymentOrderService.insertRepaymentOrder(assetRepaymentOrder);
            logger.info("添加还款表成功" + i1 + "订单号：" + assetBorrowOrder.getId());
            int i = iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrderNew);
            logger.info("更新订单状态成功" + i + "订单号：" + assetBorrowOrder.getId());

            //订单状态历史
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setCreateTime(new Date());
            assetOrderStatusHis.setOrderId(assetBorrowOrder.getId());
            /**
             * STATUS_FKCG 放款成功
             */
            assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_FKCG);

            BankAllInfo bankAllInfo = this.bankAllInfoDao.selectByPrimaryKey(Integer.parseInt(assetBorrowOrder.getUserCardType()));
//            assetOrderStatusHis.setRemark("回收金额已成功打至您的"+bankAllInfo.getBankName()+"(" +
//                    assetBorrowOrder.getUserCardCode().substring(assetBorrowOrder.getUserCardCode().length() - 4,
//                            assetBorrowOrder.getUserCardCode().length()) + ")账户，手机所有权已转移给平台");
//            iAssetOrderStatusHisDao.insertSelective(assetOrderStatusHis);
            assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setCreateTime(new Date());
            assetOrderStatusHis.setOrderId(assetBorrowOrder.getId());
            assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_FKCG);
            assetOrderStatusHis.setRemark("您的借款已成功打至您的尾号" +
                    assetBorrowOrder.getUserCardCode().substring(assetBorrowOrder.getUserCardCode().length() - 4,
                            assetBorrowOrder.getUserCardCode().length()) + "的银行卡,<br/>" +"<font color=\"#FF0000\">"+
                    DateUtils.formatDate("yyyy年MM月dd日 HH:mm:ss", assetRepaymentOrder.getRepaymentTime())
                    + "前未还款，则视为逾期,平台将收取逾期利息。</font>");
            iAssetOrderStatusHisDao.insertSelective(assetOrderStatusHis);
            // 发送短信提醒用户
            String content = "您申请的订单已审核通过，款已于" + DateUtils.getDateTime() +
                    "打至您绑定的尾号" + assetBorrowOrder.getUserCardCode().substring(assetBorrowOrder.getUserCardCode().length() - 4,
                    assetBorrowOrder.getUserCardCode().length()) +
                    "银行卡中，请在规定期内尽快处理订单。";
            
            //String result = SmsSendUtil.sendSmsDiy(assetBorrowOrder.getUserPhone(), content);
            String result = SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(assetBorrowOrder.getUserPhone(), content);
//            if ("0".equals(result)) {
                logger.info("发送代付提醒短信成功：userPhone=" + assetBorrowOrder.getUserPhone());
//            } else {
//                logger.info("发送代付提醒短信失败：userPhone=" + assetBorrowOrder.getUserPhone());
//            }

            //商户更新订单为成功，处理自己的业务逻辑
            updateAssetBorrowOrder.setStatus(AssetBorrowOrder.STATUS_YFK);
            updateAssetBorrowOrder.setPayStatus(AssetBorrowOrder.SUB_PAY_SUCC);
            updateAssetBorrowOrder.setLoanTime(new Date());
            updateAssetBorrowOrder.setLoanEndTime(assetRepaymentOrder.getRepaymentTime());
        } else if (ApiStatus.REFUND.getCode().equals(apiServiceResult.getCode())) {
            // 订单代付发生了退款 直接拒绝该笔订单
            //  商户更新订单为失败，处理自己的业务逻辑
            updateAssetBorrowOrder.setStatus(AssetBorrowOrder.STATUS_FKSB);//放款失败
            updateAssetBorrowOrder.setOrderEnd(1);//已完成
            updateAssetBorrowOrder.setPayStatus(AssetBorrowOrder.SUB_PAY_ERROR);//放款失败
        }else if(ApiStatus.FAIL.getCode().equals(apiServiceResult.getCode())) {
            updateAssetBorrowOrder.setStatus(AssetBorrowOrder.STATUS_FKSB);
            updateAssetBorrowOrder.setOrderEnd(1);
            updateAssetBorrowOrder.setPayStatus(AssetBorrowOrder.SUB_PAY_ERROR);
            
            
            //更新订单历史表
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setCreateTime(new Date());
            assetOrderStatusHis.setOrderId(assetBorrowOrder.getId());
            assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_FKSB);
            assetOrderStatusHis.setRemark("放款失败，请联系客服");
            iAssetOrderStatusHisDao.insertSelective(assetOrderStatusHis);
            
        }

        updateAssetBorrowOrder.setId(assetBorrowOrder.getId());
        updateAssetBorrowOrder.setUpdateTime(new Date());
        iAssetBorrowOrderDao.updateByPrimaryKeySelective(updateAssetBorrowOrder);

        return new ApiServiceResult();

    }

    @Override
    public ApiServiceResult queryPayForAnother(List<AssetBorrowOrder> list) throws Exception {
        for (AssetBorrowOrder assetBorrowOrder : list) {

            ApiServiceResult apiServiceResult = this.paymentService.queryOrder(assetBorrowOrder);
            logger.info("订单IDL:{} 代付订单查询结果 : {}", assetBorrowOrder.getId(), com.alibaba.fastjson.JSONObject.toJSONString(apiServiceResult));

            apiServiceResult = this.handlePayForAnotherResult(apiServiceResult, assetBorrowOrder);
            logger.info("订单IDL:{} 代付订单处理结果 : {}", assetBorrowOrder.getId(), com.alibaba.fastjson.JSONObject.toJSONString(apiServiceResult));
        }

        return new ApiServiceResult();
    }



    @Override
    public void overdueTask() {

        logger.error("逾期任务开始---------->overdue start");
        Map<String, Object> params = new HashMap<String, Object>();
        Integer statuses[] = new Integer[]{AssetOrderStatusHis.STATUS_DHK, AssetOrderStatusHis.STATUS_YYQ}; //21,23,-11,-20
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String repaymentTimeEnd = dateFormat.format(date);

        params.put("statuses", statuses);
        params.put("repaymentTimeEnd", repaymentTimeEnd);

        int count=0;

        List<AssetRepaymentOrder> repaymentList = iAssetRepaymentOrderDao.findTaskRepayment(params);
        for (AssetRepaymentOrder re : repaymentList) {
            try {
                AssetRepaymentOrder repayment = iAssetRepaymentOrderDao.getRepaymentById(re.getId());


                //防止在循环过程中有人还款或续期
                if (!repayment.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK) && repayment.getRepaymentTime().getTime() < date.getTime()) {
                	count++;
                    overdue(repayment);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("taskjob overdue error repaymentId = " + re.getId(), e);
                continue;
            }

        }
        logger.error("逾期任务结束---------->overdue end 共: " + count);


    }


    @Override
    public void overdue(AssetRepaymentOrder re) {
        Date now = new Date();
        try {
            int between = DateUtil.daysBetween(re.getRepaymentTime(), now);
            if (between > 0) {

                //本金 + 违约金
                BigDecimal originalPay = re.getMoneyAmount().add(re.getPenaltyAmount());

                //滞纳金服务费率
                BigDecimal lateFA = re.getLateFeeApr().divide(new BigDecimal(10000));

                // 滞纳金 = （本金 + 违约金） * 滞纳金服务费率 * 滞纳天数
                BigDecimal lateFee = originalPay.multiply(lateFA).multiply(new BigDecimal(between));


                //如果滞纳金>=本金，那应还滞纳金数额为本金数额(滞纳金金额不能超过本金金额)
                if (lateFee.compareTo(originalPay) >= 0) {
                    lateFee = originalPay;
                }
                // 更新repay的
                re.setLateDay(between);
                re.setInterestUpdateTime(now);
                re.setUpdateTime(now);
                re.setPlanLateFee(lateFee);
                re.setRepaymentAmount(originalPay.add(lateFee));

                // 更新用户最近一次逾期总天数、历史逾期总记录数
                if (re.getLateDay() == 1) {
                    re.setLateFeeStartTime(now);
                }

                re.setOrderStatus(AssetOrderStatusHis.STATUS_YYQ);


//				if (between == 1) {
//					//逾期第一天发短信提醒客户逾期 默
//				}

                AssetBorrowOrder borrowOrder = new AssetBorrowOrder();
                borrowOrder.setId(re.getOrderId());
                borrowOrder.setStatus(AssetOrderStatusHis.STATUS_YYQ);
                iAssetBorrowOrderDao.updateByPrimaryKeySelective(borrowOrder);


                AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
                assetOrderStatusHis.setOrderId(re.getOrderId());
                assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_YYQ);


                List<AssetOrderStatusHis> list = iAssetOrderStatusHisDao.select(assetOrderStatusHis);

                assetOrderStatusHis.setCreateTime(new Date());
                //Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
                //String penaltyAmount = re.getPenaltyAmount().toString();

                String content =  "<font color=\"#FF0000\">超时未还款，已逾期</font> " +"，<br/>剩余应还总金额"+ re.getRepaymentAmount().toString() + "元（逾期利息" + lateFee.doubleValue() + "元），请尽快还款。";
                assetOrderStatusHis.setRemark(content);
                if (null != list && list.size() > 0) {
                    assetOrderStatusHis.setId(list.get(0).getId());
                    iAssetOrderStatusHisDao.updateByPrimaryKey(assetOrderStatusHis);
                } else {
                    iAssetOrderStatusHisDao.insertSelective(assetOrderStatusHis);
                }

                // 如果未催收 进入催收  逾期第二天再推进催收系统
//				if (between == 2&&Repayment.COLLECTION_NO == re.getCollection()) {
//					try {
//						JedisDataClient.set("OVERDUE_" + re.getId(), String.valueOf(re.getId()));
//						logger.error("collection overdue success OVERDUE_" + re.getId());
//						re.setCollection(Repayment.COLLECTION_YES);
//					} catch (Exception e) {
//						logger.error("collection overdue error repaymentId=" + re.getId(), e);
//					}
//				}
                iAssetRepaymentOrderService.update(re);
            }
        } catch (ParseException e) {
            logger.error("overdue error repaymentId = " + re.getId(), e);
        }

    }


    @Override
    public void sendRemindMessage(int gapDay) {
        logger.info("提醒还款短信发送开始");
        logger.info("gapDay:" + gapDay);


        int orderSize = 0;

        int successSize = 0;

        int faliSize = 0;


        Map<String, Object> param = new HashMap<String, Object>();
        param.put("gapDay", gapDay);
        if (gapDay == 1 | gapDay == 0) {

            param.put("status", AssetOrderStatusHis.STATUS_DHK);
        } else if (gapDay == -1) {

            param.put("status", AssetOrderStatusHis.STATUS_YYQ);
        }


        List<Map<String, Object>> orderList = null;
        orderList = iAssetRepaymentOrderDao.getAssetBorrowOrder(param);


        if (null != orderList && orderList.size() > 0) {
            orderSize = orderList.size();


            for (Map<String, Object> order : orderList) {
                try {
                    logger.info("userPhone:" + order.get("userPhone").toString());
                    String content = "";
                    String card = order.get("cardCode").toString();
                    card = card.substring(card.length()-4,card.length());
                    if (gapDay == 1) {
                        content = SmsContentConstant.getTomorrowOverdue(
                                order.get("userName").toString(),order.get("repaymentAmount").toString(),card);
                    } else if (gapDay == 0) {
                        content = SmsContentConstant.getTodayOverdue(
                                order.get("userName").toString(),order.get("repaymentAmount").toString(),card);
                    } else if (gapDay == -1) {
                        content = SmsContentConstant.getOverdue(
                                order.get("userName").toString(),order.get("repaymentAmount").toString(),order.get("lateDay").toString());
                    }
                    //String result = SmsSendUtil.sendSmsDiyCLandYX(order.get("userPhone").toString(), content);
                    //String result = SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(order.get("userPhone").toString(), content);
                    String result=null;
                    if(gapDay==1 || gapDay==0)
                    {
                        result = SmsSendUtil.sendSmsDiyCLandCollection(order.get("userPhone").toString(), content);//催收类
                    }else if(gapDay==-1){
                        result = SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(order.get("userPhone").toString(), content);//通知类
                    }
                    if ("0".equals(result)) {
                        successSize++;
                    } else {
                        faliSize++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        logger.info("orderSize:" + orderSize);
        logger.info("successSize:" + successSize);

        String now = DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss");
        String content = now.concat(",").concat("本次应还:").concat(orderSize + "")
                .concat(",短信发送成功:").concat(successSize + "").concat(",短信发送失败:").concat(faliSize + "");

        logger.info(content);
    }

    @Override
    public Map fkDhCallback(Map<String, String> params) {
        Map<String, Object> ret = new HashMap();
        String dateStr = params.get("dateStr");
        String msg = "调用成功";
        List<Map<String, Object>> list = iAssetRepaymentOrderDao.getfkDhCallbackData(dateStr);
        String statusStr = "";
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                try {
                    Object lateDayObj = map.get("lateDay");
                    Object statusObj = map.get("status");
                    Integer lateDay = Integer.parseInt(lateDayObj.toString());
                    Integer status = Integer.parseInt(statusObj.toString());
                    if (Double.parseDouble(map.get("amounted").toString())>0&&map.get("order_end").toString().equals("0")) {
                        statusStr = "部分还款";
                    }else if (lateDay > 0 && status.equals(AssetOrderStatusHis.STATUS_YHK)) {
                        statusStr = "逾期已还款";
                    } else if (lateDay > 0) {
                        statusStr = "已逾期";
                    } else if (status.equals(AssetOrderStatusHis.STATUS_YHK) || status.equals(AssetOrderStatusHis.STATUS_JCHG)) {
                        statusStr = "已还款";
                    } else if (status != AssetOrderStatusHis.STATUS_DHK &&status != AssetOrderStatusHis.STATUS_JCHG && Integer.parseInt(map.get("rqc") + "") > Integer.valueOf(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SYS_FEE).get("renewal_day"))) {
                        statusStr = "续期";
                    } else {
                        statusStr = "已放款";
                    }
                    map.put("status", statusStr);

                } catch (Exception e) {

                }
            }

        }else {
            list=new ArrayList<>();
        }

        ret.put("msg", msg);
        ret.put("code", 200);
        ret.put("obj", list);
        return ret;
    }

    @Override
    public void auditOrder(Integer orderId, Integer status, String operatorRemark, String operatorAccount) {
        // 审核拒绝
        if(status == 0) {
            AssetBorrowOrder order = new AssetBorrowOrder();
            order.setId(orderId);
            order.setStatus(AssetOrderStatusHis.STATUS_SHJJ);
            order.setOrderEnd(1);
            order.setAuditTime(new Date());
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

            AssetOrderStatusHis his = new AssetOrderStatusHis();
            his.setOrderId(order.getId());
            his.setOrderStatus(AssetOrderStatusHis.STATUS_SHJJ);
            his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_SHJJ));
            his.setOperator(operatorAccount);
            his.setOperatorRemark(operatorRemark);
            his.setCreateTime(new Date());
            iAssetOrderStatusHisService.saveHis(his);
        }
        // 审核通过
        if(status == 1) {
            AssetBorrowOrder order = new AssetBorrowOrder();
            order.setId(orderId);
            order.setStatus(AssetOrderStatusHis.STATUS_DFK);
            order.setAuditTime(new Date());
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

            AssetOrderStatusHis his = new AssetOrderStatusHis();
            his.setOrderId(order.getId());
            his.setOrderStatus(AssetOrderStatusHis.STATUS_DFK);
            his.setCreateTime(new Date());
            his.setRemark("认证资料信用审核已通过");
            his.setOperator(operatorAccount);
            his.setOperatorRemark(operatorRemark);
            iAssetOrderStatusHisService.saveHis(his);

            his.setOrderStatus(AssetOrderStatusHis.STATUS_DFK);
            his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_DFK));
            his.setOperatorRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_DFK));
            his.setCreateTime(new Date());
            iAssetOrderStatusHisService.saveHis(his);
        }
    }

    @Override
    public void sendUnIdentification() {
        List<BorrowUser> list= iAssetBorrowOrderDao.getYesterdayUnIdentification();
        if(list!=null&&list.size()>0){
            logger.info("AssetBorrowOrderServiceImpl >>> sendUnIdentification 查询前一天注册，但是5项信息没有完全认证完成的客户 短信提示 开始");
            for (BorrowUser user:list){
                try {
                    logger.info("userPhone:" + user.getUserPhone());
//                    String result = SmsSendUtil.sendSmsDiy(user.getUserPhone(), SmsContentConstant.REGISTER(user.getUserAccount()));
                    String result = SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(user.getUserPhone(), SmsContentConstant.REGISTER(user.getUserAccount()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("查询前一天注册，但是5项信息没有完全认证完成的客户 短信提示 end");
        }
    }

    @Override
    public List<Map<String,String>> getOrderBlackList(Map<String, Object> param) {
        return iAssetBorrowOrderDao.getOrderBlackList(param);
    }
    @Override
    public ApiServiceResult getOrderBeforeNoEquipmentId(BorrowUser bu) {
        bu = iBorrowUserService.getBorrowUserById(bu.getId());
        OrderBeforeResult result = new OrderBeforeResult();

        Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
        String perPayMoney = loan.get("LOAN_PER");//预付款
        String penaltyAmount = loan.get("LOAN_WY");//违约金
        String moneyAmount = loan.get("LOAN_ALL");//应退还总额
        String moneyDay = loan.get("LOAN_DAY");

        //老用户提额
        if(bu.getIsOld() == 1) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", bu.getId());
            param.put("intervalDays", Integer.parseInt(loan.get("LOAN_OLD_INTERVAL_DAY"))); //实际付款与放款时间的间隔天数
            int effectCount = iAssetRepaymentOrderDao.getEffectCount(param); //提额有效的还款记录数
            if(effectCount > 0) {
                int oldAdd = Integer.parseInt(loan.get("LOAN_OLD_ADD")); //老用户提额金额
                int wyPoint = Integer.parseInt(loan.get("LOAN_OLD_WY_POINT")); //老用户违约点数
                int oldMax = Integer.parseInt(loan.get("LOAN_OLD_MAX")); //老用户提额金额
                int moneyAmountValue = Integer.valueOf(moneyAmount) + oldAdd * effectCount;
                if(moneyAmountValue > oldMax) {
                    moneyAmountValue = oldMax;
                }
                int penaltyAmountValue = moneyAmountValue * wyPoint / 100;
                int perPayMoneyValue = moneyAmountValue - penaltyAmountValue;

                perPayMoney = "" + perPayMoneyValue;
                penaltyAmount = "" + penaltyAmountValue;
                moneyAmount = "" + moneyAmountValue;
            }
        }
        result.setPerPayMoney(perPayMoney);
        result.setPenaltyAmount(penaltyAmount);
        result.setPerformanceDay(moneyDay);
        result.setUserCardCode(bu.getCardCode());

        BankAllInfo bankAllInfo = this.bankAllInfoDao.selectByPrimaryKey(bu.getCardType());
        result.setUserCardName(bankAllInfo.getBankName());

        return new ApiServiceResult<>(result);
    }

    @Override
    public List<AssetBorrowOrder> queyOrderStatus(String phone) {
        return iAssetBorrowOrderDao.queyOrderStatus(phone);

    }


}
