package com.inext.service.pay.impl;


import com.inext.constants.Constants;
import com.inext.dao.IBankAllInfoDao;
import com.inext.dao.IMakeLoansRecordDao;
import com.inext.dao.IPaymentChannelDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BankAllInfo;
import com.inext.entity.OutOrders;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IOutOrdersService;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.StringUtils;
import com.inext.utils.helipay.RSA;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lisige
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HeLiD1ServiceImpl extends AbstractPayForAnother {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBankAllInfoDao iBankAllInfoDao;

    @Resource
    private IOutOrdersService iOutOrdersService;


    @Autowired
    private IMakeLoansRecordDao makeLoansRecordDao;

    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Autowired
    IPaymentChannelDao paymentChannelDao;

    //public static final String urlTransfer = "http://transfer.trx.helipay.com/trx/transfer/interface.action";
    //public static final String REQUEST_URL_TRANSFER = "http://test.trx.helipay.com/trx/transfer/interface.action";
    public static final String REQUEST_URL_TRANSFER = "http://pay.trx.helipay.com/trx/transfer/interface.action";//正式环境
    public static final String split = "&";

    @Override
    public ApiServiceResult<PaymentChannelEnum> paymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        BankAllInfo bankAllInfo = iBankAllInfoDao.selectByPrimaryKey(Integer.parseInt(assetBorrowOrder.getUserCardType()));
        Map<String, Object> sPara = new HashMap<String, Object>();
        sPara.put("P1_bizType", "Transfer");
        sPara.put("P2_orderId", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") + assetBorrowOrder.getId());
        assetBorrowOrder.setNoOrder(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") + assetBorrowOrder.getId());
        sPara.put("P3_customerNumber", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MCHNTCD"));
        sPara.put("P4_amount", assetBorrowOrder.getPerPayMoney().toString());
        sPara.put("P5_bankCode", bankAllInfo.getBankCode());
        sPara.put("P6_bankAccountNo", assetBorrowOrder.getUserCardCode());
        sPara.put("P7_bankAccountName", assetBorrowOrder.getUserName());
        sPara.put("P8_biz", "B2C");//B2B:对公 B2C:对私
        sPara.put("P9_bankUnionCode", "");
        sPara.put("P10_feeType", "PAYER");//PAYER:付款方收取手续费 RECEIVER:收款方收取手续费
        sPara.put("P11_urgency", "true");
        sPara.put("P12_summary", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SMS).get("PROJECT_NAME"));

        StringBuffer sb = new StringBuffer();
        sb.append(split).append(sPara.get("P1_bizType"));
        sb.append(split).append(sPara.get("P2_orderId"));
        sb.append(split).append(sPara.get("P3_customerNumber"));
        sb.append(split).append(sPara.get("P4_amount"));
        sb.append(split).append(sPara.get("P5_bankCode"));
        sb.append(split).append(sPara.get("P6_bankAccountNo"));
        sb.append(split).append(sPara.get("P7_bankAccountName"));
        sb.append(split).append(sPara.get("P8_biz"));
        sb.append(split).append(sPara.get("P9_bankUnionCode"));
        sb.append(split).append(sPara.get("P10_feeType"));
        sb.append(split).append(sPara.get("P11_urgency"));
        sb.append(split).append(sPara.get("P12_summary"));


        String privateKey=Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DF_SIGNKEY_TRANSFER");
        logger.info("私钥参数 : {}", privateKey);

        sPara.put("sign", RSA.sign(sb.toString(), RSA.getPrivateKey(privateKey)));
        OutOrders orders = new OutOrders();
        String result = "";
        try {
            logger.info("代付D1参数 : {}", sPara.toString());
            result = OkHttpUtils.postForm(REQUEST_URL_TRANSFER, sPara);
            logger.info("代付D1响应 : {}", result);
            JSONObject json = JSONObject.fromObject(result);
            if (json.getString("rt2_retCode").equals("0000")) {
                // 成功
                orders.setStatus(OutOrders.STATUS_SUC);
                return new ApiServiceResult("代付请求已接受").setExt(PaymentChannelEnum.HELIPAYD1);
            } else {
                // 流转状态 可忽略
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format("订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]", assetBorrowOrder.getId(), json.getString("rt2_retCode"), json.getString("rt3_retMsg")));
            }

        } finally {
            orders.setUserId(StringUtils.toString(assetBorrowOrder.getUserId()));
            orders.setAssetOrderId(assetBorrowOrder.getId());
            orders.setOrderNo(sPara.get("P2_orderId").toString());
            orders.setOrderType(OutOrders.TYPE_HELID1);
            orders.setReqParams(JSONObject.fromObject(sPara).toString());
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.act_AgentRequest_A);
            orders.setReturnParams(result);
            orders.setUpdateTime(new Date());
            iOutOrdersService.insertSelectiveStatus(orders);
        }
    }

    @Override
    public ApiServiceResult queryPaymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        Map<String, Object> sPara = new HashMap<String, Object>();
        sPara.put("P1_bizType", "TransferQuery");
        sPara.put("P2_orderId", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") + assetBorrowOrder.getId());
        sPara.put("P3_customerNumber", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MCHNTCD"));

        StringBuffer sb = new StringBuffer();
        sb.append(split).append(sPara.get("P1_bizType"));
        sb.append(split).append(sPara.get("P2_orderId"));
        sb.append(split).append(sPara.get("P3_customerNumber"));

        sPara.put("sign", RSA.sign(sb.toString(), RSA.getPrivateKey(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_SIGNKEY_TRANSFER"))));
        String result = "";
        OutOrders orders = new OutOrders();
        try {
            logger.info("查询参数 : {}", sPara.toString());
            result = OkHttpUtils.postForm(REQUEST_URL_TRANSFER, sPara);
            logger.info("查询响应 : {}", result);
            JSONObject json = JSONObject.fromObject(result);
            if (json.get("rt7_orderStatus") != null && StringUtils.isNotEmpty(json.getString("rt7_orderStatus"))) {
                if (json.getString("rt7_orderStatus").equalsIgnoreCase("SUCCESS")) {
                    // 成功
                    orders.setStatus(OutOrders.STATUS_SUC);
                    return new ApiServiceResult("代付请求已接受");
                } else if (json.getString("rt7_orderStatus").equalsIgnoreCase("REFUND")) {
                    // 已退款
                    return new ApiServiceResult(ApiStatus.REFUND.getCode(),
                            MessageFormat.format("订单号[{0}]已退款", assetBorrowOrder.getId()));
                } else if(json.getString("rt7_orderStatus").equalsIgnoreCase("FAIL")){
                    // 放款失败
                    return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                            MessageFormat.format("订单号[{0}]放款失败", assetBorrowOrder.getId()));
                }else {
                    // 流转状态 可忽略
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format("订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]", assetBorrowOrder.getId(), json.getString("rt2_retCode"), json.getString("rt8_reason")));
                }
            } else {
                // 流转状态 可忽略
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format("订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]", assetBorrowOrder.getId(), json.getString("rt2_retCode"), json.getString("rt8_reason")));
            }
        } catch (Exception e) {
            return new ApiServiceResult(ApiStatus.ERROR.getCode(), ApiStatus.ERROR.getValue());
        }
    }
}
