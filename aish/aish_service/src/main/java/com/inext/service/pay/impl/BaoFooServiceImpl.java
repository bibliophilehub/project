package com.inext.service.pay.impl;


import com.inext.constants.Constants;
import com.inext.dao.IBankAllInfoDao;
import com.inext.dao.IPaymentChannelDao;
import com.inext.entity.*;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.IOutOrdersService;
import com.inext.utils.StringUtils;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.baofoo.XinyanAuthUtil;
import com.inext.utils.newbaofoo.demo.base.TransContent;
import com.inext.utils.newbaofoo.demo.base.request.TransReqBF0040001;
import com.inext.utils.newbaofoo.demo.base.request.TransReqBF0040002;
import com.inext.utils.newbaofoo.demo.base.response.TransRespBF0040001;
import com.inext.utils.newbaofoo.demo.base.response.TransRespBF0040002;
import com.inext.utils.newbaofoo.domain.RequestParams;
import com.inext.utils.newbaofoo.http.SimpleHttpResponse;
import com.inext.utils.newbaofoo.rsa.RsaCodingUtil;
import com.inext.utils.newbaofoo.util.BaofooClient;
import com.inext.utils.newbaofoo.util.SecurityUtil;
import com.inext.utils.newbaofoo.util.TransConstant;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * 宝付代付
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaoFooServiceImpl extends AbstractPayForAnother {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBankAllInfoDao iBankAllInfoDao;
    @Resource
    private IOutOrdersService iOutOrdersService;
    @Autowired
    IPaymentChannelDao paymentChannelDao;
    @Autowired
    XinyanAuthUtil xinyanAuthUtil;

    public static final String split = "&";


    @Override
    public ApiServiceResult<PaymentChannelEnum> paymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception {
        //查询所属银行信息
        BankAllInfo bankAllInfo = iBankAllInfoDao.selectByPrimaryKey(Integer.parseInt(assetBorrowOrder.getUserCardType()));
        //流水号
//        String transId =
//                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DF_ID_KEY") + assetBorrowOrder.getId();
        //加密参数
        String dataType = TransConstant.data_type_xml; // 数据类型 xml/json

        TransContent<TransReqBF0040001> transContent = new TransContent<>(dataType);

        List<TransReqBF0040001> trans_reqDatas = new ArrayList<>();

        TransReqBF0040001 transReqData = new TransReqBF0040001();
        transReqData.setTrans_no(assetBorrowOrder.getNoOrder());
        transReqData.setTrans_money(assetBorrowOrder.getPerPayMoney().toString());
        transReqData.setTo_acc_name(assetBorrowOrder.getUserName());
        transReqData.setTo_acc_no(assetBorrowOrder.getUserCardCode());
        transReqData.setTo_bank_name(bankAllInfo.getBankName());
//        transReqData.setTo_pro_name("上海市");
//        transReqData.setTo_city_name("上海市");
//        transReqData.setTo_acc_dept("支行");
        transReqData.setTrans_summary("代付");
        transReqData.setTrans_card_id(assetBorrowOrder.getUserIdNumber());
        transReqData.setTrans_mobile(assetBorrowOrder.getUserPhone());
        trans_reqDatas.add(transReqData);

        transContent.setTrans_reqDatas(trans_reqDatas);

        String bean2XmlString = transContent.obj2Str(transContent);
        logger.info("------baofoo---代付----请求报文：" + bean2XmlString);

        String keyStorePath = PropertiesUtil.get("baofoo_pri_key");
        String keyStorePassword = PropertiesUtil.get("baofoo_pri_key_pass");
        String pub_key = PropertiesUtil.get("baofoo_pub_key");
        String origData = bean2XmlString;
        OutOrders orders = new OutOrders();
        String reslut="";
        try {
            /*
             * 加密规则：项目编码UTF-8
             * 第一步：BASE64 加密
             * 第二步：商户私钥加密
             */
            origData =  new String(SecurityUtil.Base64Encode(origData));//Base64.encode(origData);
            String encryptData = RsaCodingUtil.encryptByPriPfxFile(origData, keyStorePath, keyStorePassword);

            logger.info("------baofoo---代付----------->【私钥加密-结果】" + encryptData);

            // 发送请求
            String requestUrl = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DAI_FU_URL");
            // 商户号
            String memberId = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DAI_FU_MER_ID");
            // 终端号
            String terminalId = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DAI_FU_TERM_ID");

            RequestParams params = new RequestParams();
            params.setMemberId(Integer.parseInt(memberId));
            params.setTerminalId(Integer.parseInt(terminalId));
            params.setDataType(dataType);
            params.setDataContent(encryptData);// 加密后数据
            params.setVersion("4.0.0");
            params.setRequestUrl(requestUrl);
            SimpleHttpResponse response = BaofooClient.doRequest(params);

            logger.info("------baofoo---代付-----------请求返回结果：" + response.getEntityString());

            TransContent<TransRespBF0040001> str2Obj = new TransContent<>(dataType);
            reslut = response.getEntityString();
            /*
             * 在商户终端正常的情况下宝付同步返回会以密文形式返回,如下：
             *
             * 此时要先宝付提供的公钥解密：RsaCodingUtil.decryptByPubCerFile(reslut, pub_key)
             *
             * 再次通过BASE64解密：new String(new Base64().decode(reslut))
             *
             * 在商户终端不正常或宝付代付系统异常的情况下宝付同步返回会以明文形式返回
             */
            //明文返回处理可能是报文头参数不正确、或其他的异常导致；
            if (reslut.contains("trans_content")) {
                //明文返回
                //我报文错误处理
                str2Obj = (TransContent<TransRespBF0040001>) str2Obj.str2Obj(reslut, TransRespBF0040001.class);
                // 业务逻辑判断
                //System.out.println(str2Obj.getTrans_head().getReturn_code()+":"+str2Obj.getTrans_head().getReturn_msg());
                if(StringUtils.equals("0000",str2Obj.getTrans_head().getReturn_code())){
                    // 成功
                    orders.setStatus(OutOrders.STATUS_SUC);
                    return new ApiServiceResult("宝付代付请求已接受").setExt(PaymentChannelEnum.BAOFOO);
                }else{
                    //失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    assetBorrowOrder.getId(),
                                    str2Obj.getTrans_head().getReturn_code(),
                                    str2Obj.getTrans_head().getReturn_msg()
                            )
                    );
                }
            } else {
                //密文返回
                //第一步：公钥解密
                reslut = RsaCodingUtil.decryptByPubCerFile(reslut, pub_key);
                //第二步BASE64解密
                reslut = SecurityUtil.Base64Decode(reslut);
                logger.info("------baofoo---代付-------解密后----返回结果" + reslut);
                str2Obj = (TransContent<TransRespBF0040001>) str2Obj.str2Obj(reslut, TransRespBF0040001.class);
                // 业务逻辑判断
                if(StringUtils.equals("0000",str2Obj.getTrans_head().getReturn_code())){
                    // 成功
                    orders.setStatus(OutOrders.STATUS_SUC);
                    return new ApiServiceResult("宝付代付请求已接受").setExt(PaymentChannelEnum.BAOFOO);
                }else{
                    //失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    assetBorrowOrder.getId(),
                                    str2Obj.getTrans_head().getReturn_code(),
                                    str2Obj.getTrans_head().getReturn_msg()
                            )
                    );
                }
            }
        } finally {
            orders.setUserId(StringUtils.toString(assetBorrowOrder.getUserId()));
            orders.setAssetOrderId(assetBorrowOrder.getId());
            orders.setOrderNo(assetBorrowOrder.getNoOrder());
            orders.setOrderType(OutOrders.TYPE_BAOFOO);
            orders.setReqParams(bean2XmlString);
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.act_AgentRequest_A);
            orders.setReturnParams(reslut);
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
        //流水号
//        String transId =
//                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DF_ID_KEY") + assetBorrowOrder.getId();

        OutOrders orders = new OutOrders();
        try {
            TransContent<TransReqBF0040002> transContent = new TransContent<>(TransConstant.data_type_xml);

            List<TransReqBF0040002> trans_reqDatas = new ArrayList<>();

            TransReqBF0040002 transReqData = new TransReqBF0040002();
            //transReqData.setTrans_batchid("23073900");
            transReqData.setTrans_no(assetBorrowOrder.getNoOrder());
            trans_reqDatas.add(transReqData);
            transContent.setTrans_reqDatas(trans_reqDatas);

            String bean2XmlString = transContent.obj2Str(transContent);
            logger.info("------baofoo---代付查询----请求报文：" + bean2XmlString);

            String keyStorePath = PropertiesUtil.get("baofoo_pri_key");
            String keyStorePassword = PropertiesUtil.get("baofoo_pri_key_pass");
            String pub_key = PropertiesUtil.get("baofoo_pub_key");
            String origData = bean2XmlString;
            //origData = Base64.encode(origData);
            /**
             * 加密规则：项目编码UTF-8
             * 第一步：BASE64 加密
             * 第二步：商户私钥加密
             */
            origData =  new String(SecurityUtil.Base64Encode(origData));//Base64.encode(origData);
            String encryptData = RsaCodingUtil.encryptByPriPfxFile(origData, keyStorePath, keyStorePassword);

            logger.info("------baofoo---代付查询----------->【私钥加密-结果】" + encryptData);

            // 发送请求
            String requestUrl = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DAI_FU_QUERY_URL");
            // 商户号
            String memberId = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DAI_FU_MER_ID");
            // 终端号
            String terminalId = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("DAI_FU_TERM_ID");
            // 数据类型 xml/json
            String dataType = TransConstant.data_type_xml;

            RequestParams params = new RequestParams();
            params.setMemberId(Integer.parseInt(memberId));
            params.setTerminalId(Integer.parseInt(terminalId));
            params.setDataType(dataType);
            params.setDataContent(encryptData);// 加密后数据
            params.setVersion("4.0.0");
            params.setRequestUrl(requestUrl);
            SimpleHttpResponse response = BaofooClient.doRequest(params);

            logger.info("------baofoo---代付查询----------请求返回结果：" + response.getEntityString());

            TransContent<TransRespBF0040002> str2Obj = new TransContent<>(dataType);

            String reslut = response.getEntityString();

            /*
             * 在商户终端正常的情况下宝付同步返回会以密文形式返回,如下：
             *
             * 此时要先宝付提供的公钥解密：RsaCodingUtil.decryptByPubCerFile(reslut, pub_key)
             *
             * 再次通过BASE64解密：new String(new Base64().decode(reslut))
             *
             * 在商户终端不正常或宝付代付系统异常的情况下宝付同步返回会以明文形式返回
             */
            if (reslut.contains("trans_content")) {
                // 我报文错误处理
                str2Obj = (TransContent<TransRespBF0040002>) str2Obj.str2Obj(reslut,TransRespBF0040002.class);
                //业务逻辑判断
                if (StringUtils.equals("0000", str2Obj.getTrans_head().getReturn_code())) {
                    if(CollectionUtils.isEmpty(str2Obj.getTrans_reqDatas()) || str2Obj.getTrans_reqDatas().get(0) == null){
                        //结果为空
                        return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                                MessageFormat.format(
                                        "订单号[{0}]中间状态 打款状态码[{1}]异常情况",
                                        assetBorrowOrder.getId(),
                                        str2Obj.getTrans_head().getReturn_code(),
                                        "Trans_reqDatas结果为空"
                                )
                        );

                    }else if(StringUtils.equals("1", str2Obj.getTrans_reqDatas().get(0).getState())){
                        // 成功（1：转账成功）
                        orders.setStatus(OutOrders.STATUS_SUC);
                        return new ApiServiceResult("宝付代付请求完成");
                    }else if(StringUtils.equals("-1", str2Obj.getTrans_reqDatas().get(0).getState())){
                        // 放款失败（-1：转账失败）
                        return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                                MessageFormat.format(
                                        "订单号[{0}]放款失败 异常情况[{1}]",
                                        assetBorrowOrder.getId(),
                                        str2Obj.getTrans_reqDatas().get(0).getTrans_remark()
                                )
                        );
                    }else{
                        /*
                         * 0：转账中
                         * 2：转账退款
                         * */
                        return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                                MessageFormat.format(
                                        "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                        assetBorrowOrder.getId(),
                                        str2Obj.getTrans_reqDatas().get(0).getState(),
                                        str2Obj.getTrans_reqDatas().get(0).getTrans_remark()
                                )
                        );
                    }
                } else {
                    //失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    assetBorrowOrder.getId(),
                                    str2Obj.getTrans_head().getReturn_code(),
                                    str2Obj.getTrans_head().getReturn_msg()
                            )
                    );
                }
            } else {
                reslut = RsaCodingUtil.decryptByPubCerFile(reslut, pub_key);
                reslut = SecurityUtil.Base64Decode(reslut);
                logger.info("------baofoo---代付查询-------解密后----返回结果" + reslut);
                str2Obj = (TransContent<TransRespBF0040002>) str2Obj.str2Obj(reslut,TransRespBF0040002.class);
                //业务逻辑判断
                if (StringUtils.equals("0000", str2Obj.getTrans_head().getReturn_code())) {
                    if(CollectionUtils.isEmpty(str2Obj.getTrans_reqDatas()) || str2Obj.getTrans_reqDatas().get(0) == null){
                        //结果为空
                        return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                                MessageFormat.format(
                                        "订单号[{0}]中间状态 打款状态码[{1}]异常情况",
                                        assetBorrowOrder.getId(),
                                        str2Obj.getTrans_head().getReturn_code(),
                                        "Trans_reqDatas结果为空"
                                )
                        );
                    }else if(StringUtils.equals("1", str2Obj.getTrans_reqDatas().get(0).getState())){
                        //1：转账成功
                        orders.setStatus(OutOrders.STATUS_SUC);
                        return new ApiServiceResult("宝付代付请求已完成");
                    }else if(StringUtils.equals("-1", str2Obj.getTrans_reqDatas().get(0).getState())){
                        // 放款失败（-1：转账失败）
                        return new ApiServiceResult(ApiStatus.FAIL.getCode(),
                                MessageFormat.format(
                                        "订单号[{0}]放款失败 异常情况[{1}]",
                                        assetBorrowOrder.getId(),
                                        str2Obj.getTrans_reqDatas().get(0).getTrans_remark()
                                )
                        );
                    }else{
                        /*
                         * 0：转账中
                         * 2：转账退款
                         * */
                        return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                                MessageFormat.format(
                                        "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                        assetBorrowOrder.getId(),
                                        str2Obj.getTrans_reqDatas().get(0).getState(),
                                        str2Obj.getTrans_reqDatas().get(0).getTrans_remark()
                                )
                        );
                    }
                } else {
                    // 失败
                    return new ApiServiceResult(ApiStatus.DEALING.getCode(),
                            MessageFormat.format(
                                    "订单号[{0}]中间状态 打款状态码[{1}]异常情况[{2}]",
                                    assetBorrowOrder.getId(),
                                    str2Obj.getTrans_head().getReturn_code(),
                                    str2Obj.getTrans_head().getReturn_msg()
                            )
                    );
                }
            }
        } catch (Exception e) {
            return new ApiServiceResult(ApiStatus.ERROR.getCode(), ApiStatus.ERROR.getValue());
        }
    }

    public static void main(String[] args) {

    }
}
