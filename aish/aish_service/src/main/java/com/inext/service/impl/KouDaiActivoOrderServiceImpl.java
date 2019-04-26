package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.inext.constants.Constants;
import com.inext.dao.IBankAllInfoDao;
import com.inext.dao.IBorrowUserDao;
import com.inext.dto.KouDaiWithdrawResultDto;
import com.inext.entity.*;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiServiceResult;
import com.inext.service.IKouDaiActivoOrderService;
import com.inext.service.IOutOrdersService;
import com.inext.utils.IdCardUtils;
import com.inext.utils.MD5Utils;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author lisige
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KouDaiActivoOrderServiceImpl implements IKouDaiActivoOrderService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IOutOrdersService outOrdersService;

    @Autowired
    private IBankAllInfoDao iBankAllInfoDao;

    @Autowired
    private IBorrowUserDao borrowUserDao;

    @Override
    public ApiServiceResult createActivoOrder(AssetBorrowOrder assetBorrowOrder) {
        BankAllInfo bankAllInfo = iBankAllInfoDao.selectByPrimaryKey(Integer.parseInt(assetBorrowOrder.getUserCardType()));
        Map<String, String> configParams = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.KOUDAI);

        final String account = configParams.get("activo_order_account");
        final String pwd = configParams.get("activo_order_pwd");
        final String url = configParams.get("activo_order_url");

        final String noOrder = assetBorrowOrder.getNoOrder();
        final Integer orderId = assetBorrowOrder.getId();
        final String userId = StringUtils.toString(assetBorrowOrder.getUserId());
        final String userName = assetBorrowOrder.getUserName();
        final String phone = assetBorrowOrder.getUserPhone();
        final String userCardCode = assetBorrowOrder.getUserCardCode();
        final String moneyDay = assetBorrowOrder.getMoneyDay() + "";
        final String orderTime = assetBorrowOrder.getOrderTime().getTime() + "";
        final String loanTime = assetBorrowOrder.getLoanTime().getTime() + "";
        final String repaymentTime = assetBorrowOrder.getLoanEndTime().getTime() + "";

        // 放款/还款金额 单位(分)
        final String perPayMoney = assetBorrowOrder.getPerPayMoney().multiply(new BigDecimal(100)).intValue() + "";
        // 年利率 18%
        final BigDecimal annualInterestRate = new BigDecimal(0.18);
        // 天数 assetBorrowOrder.getMoneyDay()
        final BigDecimal dayNumber = new BigDecimal(moneyDay);

        // 利息 按照年利率18%计算  单位(分)  利息 = 实际扣款金额*18%*期限/365 乘100转成单位分
        final String loanInterests = new BigDecimal(perPayMoney).multiply(annualInterestRate)
                .multiply(dayNumber).divide(new BigDecimal(365), 2, BigDecimal.ROUND_DOWN)
                .multiply(new BigDecimal(100)).intValue() + "";

        final String bankNumber = StringUtils.toString(bankAllInfo.getBankNumber());
        final String userIdNumber = assetBorrowOrder.getUserIdNumber();
        // 当前时间戳
        final String timestamp = System.currentTimeMillis() + "";

        // 随意获取一个用户上次的通讯录号码作为紧急联系人号码
        Map<String,Object> param = Maps.newHashMap();
        List<BorrowUserPhone> borrowUserPhones = this.borrowUserDao.queryBorrowUserPhone(param);
        String contactUsername = "";
        String contactPhone = "";
        if(borrowUserPhones != null && !borrowUserPhones.isEmpty()){
            contactUsername = borrowUserPhones.get(0).getName();
            contactPhone = borrowUserPhones.get(0).getPhone();

        }
        final String contact_username = StringUtils.isEmpty(contactUsername) ? userName : contactUsername;
        final String contact_phone = StringUtils.isEmpty(contactPhone) ? phone : contactPhone;

        // 用户信息
        Map<String, Object> userBase = new TreeMap<String, Object>() {{
            put("id_number", userIdNumber);
            put("name", userName);
            put("phone", phone);
            put("education_level", "");

            put("property", IdCardUtils.getSexByIdCard(userIdNumber));
            put("type", "2");
            put("birthday", "");
            put("contact_username", contact_username);
            put("contact_phone", contact_phone);

        }};

        // 借款订单信息
        Map<String, Object> orderBase = new TreeMap<String, Object>() {{
            put("out_trade_no", noOrder);
            put("money_amount", perPayMoney);
            put("loan_method", "0");
            put("loan_term", moneyDay);

            put("loan_interests", loanInterests);
            put("apr", "18");
            put("order_time", orderTime);
            put("loan_time", loanTime);
            put("counter_fee", "0");
            put("is_deposit", "");
            put("lend_pay_type", "1");

        }};

        // 打款银行卡必填信息
        Map<String, Object> receiveCard = new TreeMap<String, Object>() {{
            put("bank_id", bankNumber);
            put("card_no", userCardCode);
            put("phone", phone);
            put("name", userName);
            put("bank_address", "");
        }};

        // 扣款银行卡必填信息
        Map<String, Object> debitCard = new TreeMap<>();


        // 总还款信息
        Map<String, Object> repaymentBase = new TreeMap<String, Object>() {{
            put("repayment_type", "2");
            put("repayment_amount", perPayMoney);
            put("repayment_time", repaymentTime);
            put("period", "1");
            put("repayment_principal", perPayMoney);
            put("repayment_interest", "0");
        }};

        // 还款计划信息
        List<Map<String, Object>> periodBase = new ArrayList<Map<String, Object>>() {{
            add(new TreeMap<String, Object>() {{
                put("period", "1");
                put("plan_repayment_money", perPayMoney);
                put("plan_repayment_time", repaymentTime);
                put("plan_repayment_principal", perPayMoney);
                put("plan_repayment_interest", "0");
                put("apr", "18");
            }});
        }};


        Map<String, Object> params = new TreeMap<String, Object>() {{
            put("timestamp", timestamp);
            put("account", account);
            put("id_number", userIdNumber);
            put("sign", createSign(account, pwd, assetBorrowOrder.getUserIdNumber(), timestamp));

            put("user_base", userBase);
            put("order_base", orderBase);
            put("receive_card", receiveCard);
            put("debit_card", debitCard);
            put("repayment_base", repaymentBase);
            put("period_base", periodBase);
            put("other", "");
        }};


        OutOrders orders = new OutOrders();
        String paramsJson = JSONObject.toJSONString(params);
        String result = "";
        try {
            logger.info("口袋资产订单创建参数:{}", paramsJson);
            result = OkHttpUtils.post(url, paramsJson, OkHttpUtils.FORM, Constants.UTF8);
            logger.info("口袋资产订单创建返回值:{}", result);

            KouDaiWithdrawResultDto resultDto = JSONObject.parseObject(result, KouDaiWithdrawResultDto.class);
            if (resultDto.isSuccess()) {

                // 成功
                orders.setStatus(OutOrders.STATUS_SUC);
                return new ApiServiceResult("口袋资产订单创建已接受");
            } else {

                // 其它状态 等待重新发起
                return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                        MessageFormat.format("订单号[{0}] 资产订单推送状态码[{1}]异常情况[{2}]", orderId, resultDto.getCode(), resultDto.getMsg()));
            }

        } finally {
            orders.setUserId(StringUtils.toString(userId));
            orders.setAssetOrderId(orderId);
            orders.setOrderNo(noOrder);
            orders.setOrderType(OutOrders.TYPE_KOUDAI);
            orders.setReqParams(paramsJson);
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.PUSH_ACTIVO_ORDER);
            orders.setReturnParams(result);
            orders.setUpdateTime(new Date());
            outOrdersService.insertSelectiveStatus(orders);
        }

    }

    /**
     * 加签
     *
     * @param account
     * @param pwd
     * @param idCard
     * @param timestamp
     * @return
     */
    private String createSign(String account, String pwd, String idCard, String timestamp) {
        return MD5Utils.md5(account + MD5Utils.md5(pwd) + MD5Utils.md5(idCard) + timestamp);
    }
}
