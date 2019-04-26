package com.inext.service.pay.impl;


import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.dao.HcDfInfoDao;
import com.inext.dao.IBankAllInfoDao;
import com.inext.dao.IMakeLoansRecordDao;
import com.inext.dao.IPaymentChannelDao;
import com.inext.entity.*;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiResult;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IOutOrdersService;
import com.inext.utils.JsonUtils;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.StringUtils;
import com.inext.utils.XmlConverUtil;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.baofoo.XinyanAuthUtil;
import com.inext.utils.baofoo.util.JXMConvertUtil;
import com.inext.utils.helipay.RSA;
import com.inext.utils.huichaopay.HTTPClientUtils;
import com.inext.utils.huichaopay.RsaUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

/**
 * 汇潮代付
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HcServiceImpl extends AbstractPayForAnother {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBankAllInfoDao iBankAllInfoDao;
    @Resource
    private IOutOrdersService iOutOrdersService;
    @Autowired
    IPaymentChannelDao paymentChannelDao;
    @Autowired
    XinyanAuthUtil xinyanAuthUtil;
    @Autowired
    private HcDfInfoDao hcDfInfoDao;

    public static final String split = "&";


    @Override
    public ApiServiceResult<PaymentChannelEnum> paymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        //查询所属银行信息
        BankAllInfo bankAllInfo = iBankAllInfoDao.selectByPrimaryKey(Integer.parseInt(assetBorrowOrder.getUserCardType()));
        //流水号
        String transId =
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DF_ID_KEY") + assetBorrowOrder.getId();
        //商户号
        String accountNumber = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_MER_ID");
        //银行卡号
        String cardNo = assetBorrowOrder.getUserCardCode();
        //金额
        String amount = assetBorrowOrder.getPerPayMoney().toString();
        //密钥
        String priKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_PRI_KEY");
        //加密参数
        String plain = "transId=" + transId + "&accountNumber=" + accountNumber + "&cardNo=" + cardNo + "&amount=" + amount;
        //生成签名
        RsaUtils rsaUtils = RsaUtils.getInstance();
        System.out.println("汇潮代付---------生成签名前参数：" + plain);
        String signInfo = rsaUtils.signData(plain, priKey);
        System.out.println("汇潮代付---------生成签名后：" + signInfo);
        StringBuilder stringBuilder = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<yemadai>")
                .append("<accountNumber>"+accountNumber+"</accountNumber>")
                .append("<signType>RSA</signType>")
                .append("<notifyURL>"+ PropertiesUtil.get("local_url") +"/huichaopay/df/callback</notifyURL>")
                .append("<tt>1</tt>")
                .append("<transferList>")
                .append("<transId>"+ transId +"</transId>")
                .append("<bankCode>"+bankAllInfo.getOpenBank()+"</bankCode>")
                .append("<provice>上海</provice>")
                .append("<city>上海</city>")
                .append("<branchName>虹口支行</branchName>")
                .append("<accountName>" + assetBorrowOrder.getUserName() + "</accountName>")
                .append("<cardNo>" + cardNo + "</cardNo>")
                .append("<amount>" + amount + "</amount>")
                .append("<remark>代付</remark>")
                .append("<secureCode>" + signInfo + "</secureCode>")
                .append("</transferList>")
                .append("</yemadai>");

        OutOrders orders = new OutOrders();
        String result = "";
        Base64 base64 = new Base64();
        try {
            logger.info("汇潮代付参数 : {}", stringBuilder.toString());
            List<NameValuePair> nvps = Arrays.asList(
                new BasicNameValuePair(
                        "transData",
                        base64.encodeToString(stringBuilder.toString().getBytes(StandardCharsets.UTF_8))
                )
            );
            System.out.println(nvps);
            result = HTTPClientUtils.httpPostPara(
                    nvps,
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_URL")
            );

            String json = XmlConverUtil.xmltoJson(new String(base64.decode(result), StandardCharsets.UTF_8));
            logger.info("汇潮代付响应 : {}", json);
            //响应结果
            Map<String,Object> respMap =JsonUtils.parseJSON2Map(json);
            if(respMap == null || respMap.get("errCode") == null){
                // 异常处理
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]打款异常,未收到响应结果:[{1}]",
                                assetBorrowOrder.getId(),
                                json
                        )
                );
            }

            //订单处理结果
            Map<String,Object> transferList =
                    respMap.get("transferList") != null ? (Map<String,Object>)respMap.get("transferList") : null;

            if (StringUtils.equals(respMap.get("errCode").toString(), "0000")) {
                if(transferList == null || transferList.get("resCode") == null){
                     //结果为空
                     return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                             MessageFormat.format(
                                     "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                     assetBorrowOrder.getId(),
                                     respMap.get("errCode").toString(),
                                     null
                             )
                     );
                }else if(StringUtils.equals("0000",transferList.get("resCode").toString())){
                    // 成功
                    orders.setStatus(OutOrders.STATUS_SUC);
                    return new ApiServiceResult("汇潮代付请求已接受").setExt(PaymentChannelEnum.HUICHAOPAY);
                }else{
                     //失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况",
                                    assetBorrowOrder.getId(),
                                    respMap.get("errCode").toString(),
                                    null
                            )
                    );
                }
            } else {
                // 失败
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                assetBorrowOrder.getId(),
                                respMap.get("errCode").toString(),
                                null
                        )
                );
            }

        } finally {
            orders.setUserId(StringUtils.toString(assetBorrowOrder.getUserId()));
            orders.setAssetOrderId(assetBorrowOrder.getId());
            orders.setOrderNo(transId);
            orders.setOrderType(OutOrders.TYPE_HUICHAO);
            orders.setReqParams(stringBuilder.toString());
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.act_AgentRequest_A);
            orders.setReturnParams(new String(base64.decode(result), StandardCharsets.UTF_8));
            orders.setUpdateTime(new Date());
            iOutOrdersService.insertSelectiveStatus(orders);
        }
    }

    /**
     * 代扣结果查询
     * @param assetBorrowOrder
     * @return
     * @throws Exception
     */
    @Override
    public ApiServiceResult queryPaymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        //商户号
        String accountNumber = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_MER_ID");
        //密钥
        String priKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_PRI_KEY");
        //流水号
        String transId =
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DF_ID_KEY") + assetBorrowOrder.getId();
        String plain = accountNumber + "&" + String.valueOf(System.currentTimeMillis());
        RsaUtils rsaUtils = RsaUtils.getInstance();
        System.out.println("汇潮代付查询-----生成签名前参数：" + plain);
        String signInfo = rsaUtils.signData(plain, priKey);
        System.out.println("汇潮代付查询-----生成签名后：" + signInfo);

        StringBuilder stringBuilder = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<yemadai>")
                .append("<merchantNumber>" + accountNumber + "</merchantNumber>")
                .append("<signType>RSA</signType>")
                .append("<mertransferID>" + transId + "</mertransferID>")
                .append("<queryTimeBegin>0</queryTimeBegin>")
                .append("<queryTimeEnd>0</queryTimeEnd>")
                .append("<requestTime>0</requestTime>")
                .append("<sign>" + signInfo + "</sign>")
                .append("</yemadai>");


        OutOrders orders = new OutOrders();
        Base64 base64 = new Base64();
        try {
            logger.info("汇潮代付查询参数 : {}", stringBuilder.toString());
            List<NameValuePair> nvps = Arrays.asList(
                    new BasicNameValuePair(
                            "transData",
                            base64.encodeToString(stringBuilder.toString().getBytes(StandardCharsets.UTF_8))
                    )
            );
            System.out.println(nvps);
            String result = HTTPClientUtils.httpPostPara(
                    nvps,
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_QUERY_URL")
            );

            String json = XmlConverUtil.xmltoJson(new String(base64.decode(result), StandardCharsets.UTF_8));
            logger.info("汇潮代付查询响应 : {}", json);
            //更新支付方订单返回信息
            //响应结果
            Map<String,Object> respMap =JsonUtils.parseJSON2Map(json);
            if(respMap == null || respMap.get("code") == null){
                // 异常处理
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]打款异常,未收到响应结果:[{1}]",
                                assetBorrowOrder.getId(),
                                json
                        )
                );
            }
            //订单处理结果
            Map<String,Object> transfer = respMap.get("transfer") != null ? (Map<String,Object>)respMap.get("transfer") : null;

            if (StringUtils.equals(respMap.get("code").toString(), "0000")) {
                if(transfer == null || transfer.get("state") == null){
                    //结果为空
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况",
                                    assetBorrowOrder.getId(),
                                    respMap.get("errCode").toString(),
                                    null
                            )
                    );
                }else if(StringUtils.equals("00",transfer.get("state").toString())){
                    // 成功
                    orders.setStatus(OutOrders.STATUS_SUC);
                    return new ApiServiceResult(
                            MessageFormat.format("汇潮代付成功，订单号[{0}]", assetBorrowOrder.getId())
                    );
                }else if(StringUtils.equals("11",transfer.get("state").toString())){
                    // 放款失败
                    return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                            MessageFormat.format("订单号[{0}]放款失败", assetBorrowOrder.getId()));
                }else{
                    //失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    assetBorrowOrder.getId(),
                                    transfer.get("state").toString(),
                                    transfer.get("memo").toString()
                            )
                    );
                }
            } else {
                // 失败
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                assetBorrowOrder.getId(),
                                respMap.get("errCode").toString(),
                                null
                        )
                );
            }
        } catch (Exception e) {
            return new ApiServiceResult(ApiStatus.ERROR.getCode(), ApiStatus.ERROR.getValue());
        }
    }

    /**
     * 后台代付提现
     * @param operateUserId 操作人
     * @param userName 代付转账用户名
     * @param bankCardNo 银行卡号
     * @param reqAmt 交易金额
     * @param type 类型：0-back，1-app
     * @return
     * @throws Exception
     */
    public ApiServiceResult dfPayment(
            String operateUserId,
            String userName,
            String bankName,
            String bankCardNo,
            BigDecimal reqAmt,
            String type
    ) throws Exception {
        //查询银行卡所属银行
//        JSONObject jsonObject = xinyanAuthUtil.cardbin_v1(bankCardNo);//改用v1版本-2019-01-10
//        if (!jsonObject.getBoolean("success")) {
//            logger.info("新颜银行卡绑定失败" + jsonObject.getString("errorMsg"));
//            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "暂不识别此卡，请换张银行卡绑定");
//        }
//
//        JSONObject data = jsonObject.getJSONObject("data");
//        // 不允许使用信用卡绑卡
//        if(!"1".equals(data.getString("card_type"))){
//            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "不支持信用卡绑定，请更换银行卡");
//        }

//        bankName = "%" + bankName+ "%";//改用v1版本-接口取值-2019-01-10
        //查询所属银行信息
        BankAllInfo bankAllInfo = iBankAllInfoDao.findBankAllInfoByName("%" + bankName+ "%");
        if(bankAllInfo == null || StringUtils.isEmpty(bankAllInfo.getOpenBank())){
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "暂不识别此卡，请换张银行卡绑定");
        }
        //流水号
        String transId =
                Constants.BACK_CONFIG_PARAMS_MAP
                        .get(BackConfigParams.HUICHAO).get("DF_ID_KEY") + System.currentTimeMillis();
        //商户号
        String accountNumber = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_MER_ID");
        //银行卡号
        //String cardNo = bankCardNo;
        //金额
        String amount = reqAmt.toString();
        //密钥
        String priKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_PRI_KEY");

        //新增代付流水记录
        HcDfInfo info = new HcDfInfo();
        info.setOperateUserId(operateUserId);
        info.setAssetOrderId(null);
        info.setUserName(userName);
        info.setBankCardNo(bankCardNo);
        info.setOrderNo(transId);
        info.setReqAmt(amount);
        info.setType(type);
        info.setAddTime(new Date());
        info.setUpdateTime(new Date());
        info.setStatus("0");
        hcDfInfoDao.insert(info);

        //加密参数
        String plain = "transId=" + transId + "&accountNumber=" + accountNumber + "&cardNo=" + bankCardNo + "&amount=" + amount;
        //生成签名
        RsaUtils rsaUtils = RsaUtils.getInstance();
        System.out.println("汇潮代付---------生成签名前参数：" + plain);
        String signInfo = rsaUtils.signData(plain, priKey);
        System.out.println("汇潮代付---------生成签名后：" + signInfo);
        StringBuilder stringBuilder = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<yemadai>")
                .append("<accountNumber>"+accountNumber+"</accountNumber>")
                .append("<signType>RSA</signType>")
                .append("<notifyURL>"+ PropertiesUtil.get("local_url") +"/huichaopay/df/callback</notifyURL>")
                .append("<tt>1</tt>")
                .append("<transferList>")
                .append("<transId>"+ transId +"</transId>")
                .append("<bankCode>"+bankAllInfo.getOpenBank()+"</bankCode>")
                .append("<provice>浙江</provice>")
                .append("<city>温州</city>")
                .append("<branchName>温州市分行</branchName>")
                .append("<accountName>" + userName + "</accountName>")
                .append("<cardNo>" + bankCardNo + "</cardNo>")
                .append("<amount>" + amount + "</amount>")
                .append("<remark>提现</remark>")
                .append("<secureCode>" + signInfo + "</secureCode>")
                .append("</transferList>")
                .append("</yemadai>");

        String result = "";
        Base64 base64 = new Base64();
        try {
            logger.info("汇潮代付参数 : {}", stringBuilder.toString());
            List<NameValuePair> nvps = Arrays.asList(
                    new BasicNameValuePair(
                            "transData",
                            base64.encodeToString(stringBuilder.toString().getBytes(StandardCharsets.UTF_8))
                    )
            );
            System.out.println(nvps);
            result = HTTPClientUtils.httpPostPara(
                    nvps,
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_URL")
            );

            String json = XmlConverUtil.xmltoJson(new String(base64.decode(result), StandardCharsets.UTF_8));
            logger.info("汇潮代付响应 : {}", json);
            //响应结果
            Map<String,Object> respMap =JsonUtils.parseJSON2Map(json);
            if(respMap == null || respMap.get("errCode") == null){
                // 异常处理
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]打款异常,未收到响应结果:[{1}]",
                                transId,
                                json
                        )
                );
            }

            //订单处理结果
            Map<String,Object> transferList =
                    respMap.get("transferList") != null ? (Map<String,Object>)respMap.get("transferList") : null;

            if (StringUtils.equals(respMap.get("errCode").toString(), "0000")) {
                if(transferList == null || transferList.get("resCode") == null){
                    //结果为空
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    transId,
                                    respMap.get("errCode").toString(),
                                    null
                            )
                    );
                }else if(StringUtils.equals("0000",transferList.get("resCode").toString())){
                    // 成功
//                    info.setStatus(HcDfInfo.STATUS_SUC);
                    return new ApiServiceResult("汇潮代付请求已接受").setExt(PaymentChannelEnum.HUICHAOPAY);
                }else{
                    // 失败
                    info.setStatus(HcDfInfo.STATUS_OTHER);
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况",
                                    transId,
                                    respMap.get("errCode").toString(),
                                    null
                            )
                    );
                }
            } else {
                // 失败
                info.setStatus(HcDfInfo.STATUS_OTHER);
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                transId,
                                respMap.get("errCode").toString(),
                                null
                        )
                );
            }
        } finally {
            info.setReqParams(stringBuilder.toString());
            info.setReturnParams(new String(base64.decode(result), StandardCharsets.UTF_8));
            info.setUpdateTime(new Date());
            hcDfInfoDao.updateByPrimaryKeySelective(info);
        }
    }

    /**
     * 同步代付结果
     * @param orderNo 订单号
     * @return
     * @throws Exception
     */
    public ApiServiceResult queryDfPayment(String orderNo) throws Exception {
        //商户号
        String accountNumber = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_MER_ID");
        //密钥
        String priKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_PRI_KEY");
        //流水号
//        String transId =
//                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DF_ID_KEY") + orderNo;
        String plain = accountNumber + "&" + String.valueOf(System.currentTimeMillis());
        RsaUtils rsaUtils = RsaUtils.getInstance();
        System.out.println("汇潮代付查询-----生成签名前参数：" + plain);
        String signInfo = rsaUtils.signData(plain, priKey);
        System.out.println("汇潮代付查询-----生成签名后：" + signInfo);

        StringBuilder stringBuilder = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<yemadai>")
                .append("<merchantNumber>" + accountNumber + "</merchantNumber>")
                .append("<signType>RSA</signType>")
                .append("<mertransferID>" + orderNo + "</mertransferID>")
                .append("<queryTimeBegin>0</queryTimeBegin>")
                .append("<queryTimeEnd>0</queryTimeEnd>")
                .append("<requestTime>0</requestTime>")
                .append("<sign>" + signInfo + "</sign>")
                .append("</yemadai>");


        HcDfInfo info = new HcDfInfo();

        Base64 base64 = new Base64();
        try {
            logger.info("汇潮代付查询参数 : {}", stringBuilder.toString());
            List<NameValuePair> nvps = Arrays.asList(
                    new BasicNameValuePair(
                            "transData",
                            base64.encodeToString(stringBuilder.toString().getBytes(StandardCharsets.UTF_8))
                    )
            );
            System.out.println(nvps);
            String result = HTTPClientUtils.httpPostPara(
                    nvps,
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("DAI_FU_QUERY_URL")
            );

            String json = XmlConverUtil.xmltoJson(new String(base64.decode(result), StandardCharsets.UTF_8));
            logger.info("汇潮代付查询响应 : {}", json);
            //更新支付方订单返回信息
            //响应结果
            Map<String,Object> respMap =JsonUtils.parseJSON2Map(json);
            if(respMap == null || respMap.get("code") == null){
                // 异常处理
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]打款异常,未收到响应结果:[{1}]",
                                orderNo,
                                json
                        )
                );
            }
            //订单处理结果
            Map<String,Object> transfer = respMap.get("transfer") != null ? (Map<String,Object>)respMap.get("transfer") : null;

            if (StringUtils.equals(respMap.get("code").toString(), "0000")) {
                if(transfer == null || transfer.get("state") == null){
                    //结果为空
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况",
                                    orderNo,
                                    respMap.get("code").toString(),
                                    null
                            )
                    );
                }else if(StringUtils.equals("00",transfer.get("state").toString())){
                    // 成功
                    info.setStatus(HcDfInfo.STATUS_SUC);
                    info.setNotifyParams(new String(base64.decode(result), StandardCharsets.UTF_8));
                    info.setNotifyTime(new Date());
                    hcDfInfoDao.updateByPrimaryKeySelective(info);
                    return new ApiServiceResult("汇潮代付请求已完成");
                }else if(StringUtils.equals("11",transfer.get("state").toString())){
                    // 放款失败
                    info.setStatus(HcDfInfo.STATUS_OTHER);
                    info.setNotifyParams(new String(base64.decode(result), StandardCharsets.UTF_8));
                    info.setNotifyTime(new Date());
                    hcDfInfoDao.updateByPrimaryKeySelective(info);
                    return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                            MessageFormat.format("订单号[{0}]打款失败", orderNo));
                }else{
                    // 放款失败
                    info.setStatus(HcDfInfo.STATUS_OTHER);
                    info.setNotifyParams(new String(base64.decode(result), StandardCharsets.UTF_8));
                    info.setNotifyTime(new Date());
                    hcDfInfoDao.updateByPrimaryKeySelective(info);
                    //失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    orderNo,
                                    transfer.get("state").toString(),
                                    transfer.get("memo").toString()
                            )
                    );
                }
            } else {
                // 失败
                info.setStatus(HcDfInfo.STATUS_OTHER);
                info.setNotifyParams(new String(base64.decode(result), StandardCharsets.UTF_8));
                info.setNotifyTime(new Date());
                hcDfInfoDao.updateByPrimaryKeySelective(info);
                return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                        MessageFormat.format(
                                "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                orderNo,
                                respMap.get("code").toString(),
                                null
                        )
                );
            }
        } catch (Exception e) {
            return new ApiServiceResult(ApiStatus.ERROR.getCode(), ApiStatus.ERROR.getValue());
        }
    }



    public static void main(String[] args) {

    }
}
