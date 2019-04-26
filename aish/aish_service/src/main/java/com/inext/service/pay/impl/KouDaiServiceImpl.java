package com.inext.service.pay.impl;


import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.dao.IAssetBorrowOrderDao;
import com.inext.dao.IBankAllInfoDao;
import com.inext.dao.IMakeLoansRecordDao;
import com.inext.dao.IPaymentChannelDao;
import com.inext.dto.KouDaiWithdrawQueryResultDto;
import com.inext.dto.KouDaiWithdrawResultDto;
import com.inext.entity.*;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IOutOrdersService;
import com.inext.utils.CompareUtils;
import com.inext.utils.DateUtil;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author lisige
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KouDaiServiceImpl extends AbstractPayForAnother {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBankAllInfoDao iBankAllInfoDao;

    @Autowired
    private IOutOrdersService iOutOrdersService;



    @Override
    public ApiServiceResult<PaymentChannelEnum> paymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        BankAllInfo bankAllInfo = iBankAllInfoDao.selectByPrimaryKey(Integer.parseInt(assetBorrowOrder.getUserCardType()));
        Map<String, String> configParams = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.KOUDAI);

        final String projectName = configParams.get("project_name");
        final String pwd = configParams.get("pwd");
        final String signKey = configParams.get("sign_key");
        final String url = configParams.get("withdraw_url");

        final String noOrder = assetBorrowOrder.getNoOrder();
        final Integer orderId = assetBorrowOrder.getId();
        final String userId = StringUtils.toString(assetBorrowOrder.getUserId());
        final String userName = assetBorrowOrder.getUserName();
        final String userCardCode = assetBorrowOrder.getUserCardCode();
        final String perPayMoney = assetBorrowOrder.getPerPayMoney().multiply(new BigDecimal(100)).intValue() + "";
        final String bankNumber = StringUtils.toString(bankAllInfo.getBankNumber());


        Map<String, Object> params = new TreeMap<String, Object>() {{
            put("project_name", projectName);
            put("pwd", pwd);
            put("yur_ref", noOrder);
            put("user_id", userId);
            put("real_name", userName);
            put("bank_id", bankNumber);
            put("card_no", userCardCode);
            // 参数是以分为单位
            put("money", perPayMoney);
            put("fee", "0");
            put("pay_summary", "花小侠放款");
        }};

        params.put("sign", createSign(params, signKey));

        String param = "";
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            param += formatFormStr(entry.getKey(), entry.getValue());
        }
        param = param.substring(0, param.length() - 1);

        OutOrders orders = new OutOrders();
        String paramsJson = JSONObject.toJSONString(params);
        String result = "";
        try {
            logger.info("口袋打款参数:{}", paramsJson);
            result = OkHttpUtils.post(url, param, OkHttpUtils.FORM, Constants.UTF8);
            logger.info("口袋打款返回值:{}", result);

            KouDaiWithdrawResultDto resultDto = JSONObject.parseObject(result, KouDaiWithdrawResultDto.class);
            if (resultDto.isSuccess()) {

                // 成功
                orders.setStatus(OutOrders.STATUS_SUC);
                return new ApiServiceResult("代付请求已接受").setExt(PaymentChannelEnum.KOUDAI);
            } else {

                // 其它状态 等待重新发起
                return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                        MessageFormat.format("订单号[{0}] 打款状态码[{1}]异常情况[{2}]", orderId, resultDto.getCode(), resultDto.getMsg()));
            }

        } finally {
            orders.setUserId(StringUtils.toString(userId));
            orders.setAssetOrderId(orderId);
            orders.setOrderNo(noOrder);
            orders.setOrderType(OutOrders.TYPE_KOUDAI);
            orders.setReqParams(paramsJson);
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.act_AgentRequest_A);
            orders.setReturnParams(result);
            orders.setUpdateTime(new Date());
            iOutOrdersService.insertSelectiveStatus(orders);
        }
    }


    public static void main(String[] args) throws Exception {
        // System.err.println(KouDaiServiceImpl.class.getSimpleName());
        // System.err.println(HeLiServiceImpl.class.getName());
        //
        // IPayForAnotherService paymentService = new HeLiServiceImpl();
        // System.err.println(paymentService.getClass().getSimpleName());


        // new KouDaiServiceImpl().queryPaymentApi(new AssetBorrowOrder());
    }

    @Override
    public ApiServiceResult queryPaymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        Map<String, String> configParams = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.KOUDAI);

        final String projectName = configParams.get("project_name");
        final String signKey = configParams.get("sign_key");
        final String url = configParams.get("withdraw_query_url");
        // final String projectName = "koudai_lehuigou_2017";
        // final String signKey = "**kdpay_koudai_lehuigou_2017**";
        // final String url = "http://test.withdraw.koudailc.com:8081/withdraw/query-one-v2";

        // final String noOrder = "20180903142600";
        final String noOrder = assetBorrowOrder.getNoOrder();
        final Integer orderId = assetBorrowOrder.getId();
        final Integer userId = assetBorrowOrder.getUserId();

        Map<String, Object> params = new TreeMap<String, Object>() {{
            put("project_name", projectName);
            put("yur_ref", noOrder);
        }};

        params.put("sign", createSign(params, signKey));

        String param = "";
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            param += formatFormStr(entry.getKey(), entry.getValue());
        }
        param = param.substring(0, param.length() - 1);

        OutOrders orders = new OutOrders();
        String paramsJson = JSONObject.toJSONString(params);
        String result = "";
        try {
            logger.info("口袋打款查询参数:{}", paramsJson);
            result = OkHttpUtils.post(url, param, OkHttpUtils.FORM, Constants.UTF8);
            logger.info("口袋打款查询返回值:{}", result);

            KouDaiWithdrawQueryResultDto resultDto = JSONObject.parseObject(result, KouDaiWithdrawQueryResultDto.class);
            if (resultDto.isSuccess()) {

                // 成功
                orders.setStatus(OutOrders.STATUS_SUC);
                return new ApiServiceResult("代付请求已完成,打款成功");
            } else {

                // 其它状态 等待重新发起
                return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                        MessageFormat.format("订单号[{0}] 打款查询状态码[{1}]异常情况[{2}]", orderId, resultDto.getCode(), resultDto.getMsg()));
            }

        } finally {
            orders.setUserId(StringUtils.toString(userId));
            orders.setAssetOrderId(orderId);
            orders.setOrderNo(noOrder);
            orders.setOrderType(OutOrders.TYPE_KOUDAI);
            orders.setReqParams(paramsJson);
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.QUERY_ORDER);
            orders.setReturnParams(result);
            orders.setUpdateTime(new Date());
            iOutOrdersService.insertSelectiveStatus(orders);
        }
    }


    /**
     * 将字符拼接为form表单提交格式
     * 请注意最后一个字符串存在一个多余的&
     *
     * @param key
     * @param value
     * @return
     */
    public static String formatFormStr(String key, Object value) {
        return key + "=" + value + "&";
    }

    /**
     * 生成签名字符串
     *
     * @param params
     * @return
     */
    public static String createSign(Map<String, Object> params, String key) throws UnsupportedEncodingException {

        String str = "";
        //对请求参数进行urlencode加密
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            params.put(entry.getKey(), URLEncoder.encode(StringUtils.toString(entry.getValue()), "utf-8"));
        }

        List<Map.Entry<String, Object>> sortMap = new ArrayList<>(params.entrySet());

        for (int i = 0; i < sortMap.size(); i++) {
            str += formatFormStr(sortMap.get(i).getKey(), sortMap.get(i).getValue());
        }
        //清除最后一个&
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        str += key;
        //添加要进行计算摘要的信息
        return new String(Base64.encodeBase64(str.getBytes()));
    }
}
