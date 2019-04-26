package com.inext.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.dao.IAssetBorrowOrderDao;
import com.inext.dao.IAssetOrderStatusHisDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.BackConfigParams;
import com.inext.entity.OutOrders;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiServiceResult;
import com.inext.service.ILianLianPayService;
import com.inext.service.IOutOrdersService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import com.inext.utils.OrderNoUtil;
import com.inext.utils.baofoo.GenerateNo;
import com.inext.utils.lianpay.bean.PaymentRequestBean;
import com.inext.utils.lianpay.bean.PaymentResponseBean;
import com.inext.utils.lianpay.bean.QueryPaymentRequestBean;
import com.inext.utils.lianpay.bean.QueryPaymentResponseBean;
import com.inext.utils.lianpay.constant.PaymentConstant;
import com.inext.utils.lianpay.constant.PaymentStatusEnum;
import com.inext.utils.lianpay.constant.RetCodeEnum;
import com.inext.utils.lianpay.util.HttpUtil;
import com.inext.utils.lianpay.util.SignUtil;
import com.lianlianpay.security.utils.LianLianPaySecurity;
import com.lianpay.api.util.TraderRSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("lianLianPayService")
public class LianLianPayServiceImpl implements ILianLianPayService {
    private static final Logger logger = LoggerFactory.getLogger(LianLianPayServiceImpl.class);

    @Resource
    private IOutOrdersService iOutOrdersService;
    @Resource
    private IAssetBorrowOrderDao iAssetBorrowOrderDao;
    @Resource
    private IAssetOrderStatusHisDao iAssetOrderStatusHisDao;

    @Override
    public ApiServiceResult paymentApi(AssetBorrowOrder assetBorrowOrder) {

        ApiServiceResult apiServiceResult = new ApiServiceResult();
        logger.info("进入——paymentApi");
        try {
            String noOrder = OrderNoUtil.payRecordNo();
            // 商户测试期间需要用正式的数据测试，测试时默认单笔单日单月额度50，等测试OK，申请走上线流程打开额度
            PaymentRequestBean paymentRequestBean = new PaymentRequestBean();

            paymentRequestBean.setNo_order(noOrder);
            paymentRequestBean.setDt_order(DateUtils.getDate(DateUtils.timePattern2));
            //付款金额 单位为元 精确到小数点后两位  测试写死1.00
            paymentRequestBean.setMoney_order(assetBorrowOrder.getPerPayMoney()+"");
            paymentRequestBean.setCard_no(assetBorrowOrder.getUserCardCode());
            paymentRequestBean.setAcct_name(assetBorrowOrder.getUserName());
//         paymentRequestBean.setBank_name(assetBorrowOrder.getUserCardType());
            paymentRequestBean.setInfo_order("回收手机价钱");
            paymentRequestBean.setFlag_card("0");
            paymentRequestBean.setMemo("代付");
            // 填写商户自己的接收付款结果回调异步通知
            paymentRequestBean.setNotify_url(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("BACK_PATH")+"/lian/tradepayapi/receiveNotify");
            paymentRequestBean.setOid_partner(PaymentConstant.OID_PARTNER);
            paymentRequestBean.setPlatform("quhuigou");
            paymentRequestBean.setApi_version(PaymentConstant.API_VERSION);
            paymentRequestBean.setSign_type(PaymentConstant.SIGN_TYPE);
            // 用商户自己的私钥加签
            logger.info("私钥加签---1");
            paymentRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(paymentRequestBean))));
            logger.info("私钥加签---3");
            String jsonStr = JSON.toJSONString(paymentRequestBean);
            logger.info("实时付款请求报文：" + jsonStr);

            String tablelastName = DateUtil.getDateFormat("yyyyMMdd");
            // 插入订单
            OutOrders orders = new OutOrders();
            orders.setOrderNo(GenerateNo.nextOrdId());
            orders.setOrderType(OutOrders.TYPE_LIANLIAN);
            orders.setReqParams(jsonStr);
            orders.setAddTime(new Date());
            orders.setAct(OutOrders.act_AgentRequest_A);//请求
            orders.setStatus(OutOrders.STATUS_WAIT);
            orders.setTablelastName(tablelastName);
            iOutOrdersService.insertByTablelastName(orders);


            // 用银通公钥对请求参数json字符串加密
            // 报Illegal key
            // size异常时，可参考这个网页解决问题http://www.wxdl.cn/java/security-invalidkey-exception.html
            String encryptStr = null;

            encryptStr = LianLianPaySecurity.encrypt(jsonStr, PaymentConstant.PUBLIC_KEY_ONLINE);

            if (StringUtils.isEmpty(encryptStr)) {
                // 加密异常
                apiServiceResult.setCode("500");
                apiServiceResult.setMsg("加密异常");
                logger.error("加密异常:");
                return apiServiceResult;
            }
            //保存流水号
            assetBorrowOrder.setNoOrder(noOrder);
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);
            logger.info(assetBorrowOrder.getUserName() + "保存商户流水号=======" + noOrder);
            JSONObject json = new JSONObject();
            json.put("oid_partner", PaymentConstant.OID_PARTNER);
            json.put("pay_load", encryptStr);
            String response = HttpUtil.doPost("https://instantpay.lianlianpay.com/paymentapi/payment.htm", json, "UTF-8");
            logger.info("付款接口返回响应报文：" + response);

            // 更新订单
            OutOrders ordersNew = new OutOrders();
            ordersNew.setId(orders.getId());

            // ordersNew.setOrderNo(borrowOrder.getOutTradeNo());
            ordersNew.setReturnParams(response);
            ordersNew.setUpdateTime(new Date());
            ordersNew.setStatus(OutOrders.STATUS_SUC);
            ordersNew.setTablelastName(tablelastName);
            iOutOrdersService.updateByTablelastName(ordersNew);
            apiServiceResult = processPayResult(response);

        } catch (Exception e) {
            e.printStackTrace();
            apiServiceResult.setCode("500");
            apiServiceResult.setMsg("加密异常");
            logger.error("系统异常:=========代付" + e);

        } finally {
            return apiServiceResult;
        }

    }

    @Override
    public ApiServiceResult queryPaymentApi(AssetBorrowOrder assetBorrowOrder) {
        ApiServiceResult apiServiceResult = new ApiServiceResult();
        String ext = "";
        String remark = "";
        Boolean flag = false;
        //订单状态历史
        AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
        assetOrderStatusHis.setCreateTime(new Date());
        assetOrderStatusHis.setOrderId(assetBorrowOrder.getId());
        // 订单
        AssetBorrowOrder assetBorrowOrderNew = new AssetBorrowOrder();
        assetBorrowOrderNew.setUpdateTime(new Date());
        assetBorrowOrderNew.setId(assetBorrowOrder.getId());
        try {

            // 连连内部测试环境数据
            QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
            queryRequestBean.setNo_order(assetBorrowOrder.getNoOrder());
            queryRequestBean.setOid_partner(PaymentConstant.OID_PARTNER);
            queryRequestBean.setApi_version(PaymentConstant.API_VERSION);
            queryRequestBean.setSign_type(PaymentConstant.SIGN_TYPE);
            queryRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(queryRequestBean))));

//        System.out.println(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(queryRequestBean))));

            JSONObject json = JSON.parseObject(JSON.toJSONString(queryRequestBean));
            String queryResult = HttpUtil.doPost("https://instantpay.lianlianpay.com/paymentapi/queryPayment.htm", json,
                    "UTF-8");
            logger.info("实时付款查询接口响应报文：" + queryResult);


            if (StringUtils.isEmpty(queryResult)) {
                // 可抛异常，查看原因
                logger.error("实时付款查询接口响应异常");
                assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_FKZ));
                assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_NERWORK_ERROR);
                assetBorrowOrderNew.setPayRemark("实时付款查询接口响应异常");
                return apiServiceResult = new ApiServiceResult(ApiStatus.ERROR.getCode(), "实时付款查询接口响应异常");

            }
            QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(queryResult,
                    QueryPaymentResponseBean.class);

            // 先对结果验签
            boolean signCheck = TraderRSAUtil.checksign(PaymentConstant.PUBLIC_KEY_ONLINE,
                    SignUtil.genSignData(JSONObject.parseObject(queryResult)), queryPaymentResponseBean.getSign());
            if (!signCheck) {
                // 传送数据被篡改，可抛出异常，再人为介入检查原因
                logger.error("返回结果验签异常,可能数据被篡改");
                assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_FKZ));
                assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_NERWORK_ERROR);
                assetBorrowOrderNew.setPayRemark("返回结果验签异常,可能数据被篡改");
                return apiServiceResult = new ApiServiceResult(ApiStatus.ERROR.getCode(), "返回结果验签异常,可能数据被篡改");
            }
            // 判断流水号是否一致
            ext = queryPaymentResponseBean.getNo_order();
            if (queryPaymentResponseBean.getRet_code().equals("0000") && ext.equals(assetBorrowOrder.getNoOrder())) {
                logger.info("流水号一致："+ext);
                PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum
                        .getPaymentStatusEnumByValue(queryPaymentResponseBean.getResult_pay());

//          固定成功  PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.getPaymentStatusEnumByValue("SUCCESS");
                // TODO商户根据订单状态处理自已的业务逻辑
                switch (paymentStatusEnum) {
                    case PAYMENT_APPLY:
                        // 付款申请，这种情况一般不会发生，如出现，请直接找连连技术处理
                        assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_FKZ));
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_NERWORK_ERROR);
                        assetBorrowOrderNew.setPayRemark("返回结果验签异常,可能数据被篡改");
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "直接找连连技术处理");
                        break;
                    case PAYMENT_CHECK:
                        // 复核状态
                        // 返回4002，4003，4004时，订单会处于复核状态，这时还未创建连连支付单，没提交到银行处理，如需对该订单继续处理，需商户先人工审核这笔订单是否是正常的付款请求，没问题后再调用确认付款接口
                        // 如果对于复核状态的订单不做处理，可当做失败订单
                        assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_FKSB));
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.STATUS_GO_SUBMIT);
                        assetBorrowOrder.setOrderEnd(1);
                        assetBorrowOrderNew.setPayRemark("复核状态的订单");
                        //订单状态历史
                        assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_SHJJ);
                        assetOrderStatusHis.setRemark("放款审核不通过");
                        flag = true;
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "复核状态的订单");
                        break;
                    case PAYMENT_SUCCESS:
                        // 成功
                        assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_YFK));
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_PAY_SUCC);
                        assetBorrowOrderNew.setPayRemark("代发成功");
                        //成功时 订单状态在回调后处理
                        apiServiceResult = new ApiServiceResult(ApiStatus.SUCCESS.getCode(), "代发成功");
                        break;
                    case PAYMENT_FAILURE:
                        // 失败
                        assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_FKSB));
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_PAY_ERROR);
                        assetBorrowOrder.setOrderEnd(1);
                        assetBorrowOrderNew.setPayRemark("代发失败");
                        //订单状态历史
                        assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_SHJJ);
                        assetOrderStatusHis.setRemark("审核拒绝");
                        flag = true;
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "代发失败");
                        break;
                    case PAYMENT_DEALING:
                        // 处理中
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                        assetBorrowOrderNew.setPayRemark("查询后还在处理中");
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "查询后还在处理中");
                        break;
                    case PAYMENT_RETURN:
                        // 退款
                        // 可当做失败（退款情况
                        // 极小概率下会发生，个别银行处理机制是先扣款后打款给用户时再检验卡号信息是否正常，异常时会退款到连连商户账上）
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_PAY_ERROR);
                        assetBorrowOrderNew.setPayRemark("代发失败");
                        //订单状态历史
                        assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_SHJJ);
                        assetOrderStatusHis.setRemark("审核拒绝");
                        flag = true;
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "代发失败");
                        break;
                    case PAYMENT_CLOSED:
                        // 关闭  可当做失败 ，对于复核状态的订单不做处理会将订单关闭
                        assetBorrowOrderNew.setStatus(Integer.valueOf(AssetBorrowOrder.STATUS_FKSB));
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_PAY_ERROR);
                        assetBorrowOrder.setOrderEnd(1);
                        assetBorrowOrderNew.setPayRemark("代发失败");
                        //订单状态历史
                        assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_SHJJ);
                        assetOrderStatusHis.setRemark("审核拒绝");
                        flag = true;
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "代发失败");
                        break;
                    default:
                        assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                        assetBorrowOrderNew.setPayRemark("查询后还在处理中");
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "查询后还在处理中");
                        break;
                }
            } else if (queryPaymentResponseBean.getRet_code().equals("8901")) {
                // 订单不存在，这种情况可以用原单号付款，最好不要换单号，如换单号，在连连商户站确认下改订单是否存在，避免系统并发时返回8901，实际有一笔订单
                logger.info("流水号不一致时8901");
                assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.STATUS_NOT_HAVE);
                assetBorrowOrderNew.setPayRemark("订单不存在");
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "订单不存在");
            } else {
                logger.info("流水号不一致时最终结果");
                // 查询异常（极端情况下才发生,对于这种情况，可人工介入查询，在连连商户站查询或者联系连连客服，查询订单状态）
                assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_NERWORK_ERROR);
                assetBorrowOrderNew.setPayRemark("查询异常");
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "查询异常");
                logger.error("查询异常");
            }
        } catch (Exception e) {
            assetBorrowOrderNew.setPayStatus(AssetBorrowOrder.SUB_NERWORK_ERROR);
            assetBorrowOrderNew.setPayRemark("查询异常");
            apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "查询异常");
            logger.error("查询异常" + e);
        } finally {
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrderNew);
            if (flag) {
                iAssetOrderStatusHisDao.insertSelective(assetOrderStatusHis);
            }
            return apiServiceResult;
        }


    }


    /**
     * //处理连连实时返回结果
     *
     * @param response
     * @return
     */
    public static ApiServiceResult processPayResult(String response) {
        ApiServiceResult apiServiceResult = new ApiServiceResult(AssetBorrowOrder.SUB_NERWORK_ERROR);
        try {


            if (StringUtils.isEmpty(response)) {
                // 出现异常时调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
                apiServiceResult.setCode(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                apiServiceResult.setMsg("支付未知异常，请查询支付结果确认支付状态");
            } else {
                PaymentResponseBean paymentResponseBean = JSONObject.parseObject(response, PaymentResponseBean.class);
                //获取流水号
                String noOrder = paymentResponseBean.getNo_order();

                // 对返回0000时验证签名
                if (paymentResponseBean.getRet_code().equals("0000")) {
                    // 先对结果验签
                    boolean signCheck = TraderRSAUtil.checksign(PaymentConstant.PUBLIC_KEY_ONLINE,
                            SignUtil.genSignData(JSONObject.parseObject(response)), paymentResponseBean.getSign());
                    if (!signCheck) {
                        // 传送数据被篡改，可抛出异常，再人为介入检查原因
                        apiServiceResult.setCode(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                        apiServiceResult.setMsg("返回结果验签异常,可能数据被篡改");
                        logger.error("返回结果验签异常,可能数据被篡改");
                    }
                    logger.info(paymentResponseBean.getNo_order() + "订单处于付款处理中");
                    // 已生成连连支付单，付款处理中（交易成功，不是指付款成功，是指跟连连流程正常），商户可以在这里处理自已的业务逻辑（或者不处理，在异步回调里处理逻辑）,最终的付款状态由异步通知回调告知
                    apiServiceResult.setCode(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                    apiServiceResult.setMsg(paymentResponseBean.getNo_order() + "订单处于付款处理中");
                } else if (paymentResponseBean.getRet_code().equals("4002")
                        || paymentResponseBean.getRet_code().equals("4003")
                        || paymentResponseBean.getRet_code().equals("4004")) {
                    // 当调用付款申请接口返回4002，4003，4004,会同时返回验证码，用于确认付款接口
                    // 对于疑似重复订单，需先人工审核这笔订单是否正常的付款请求，而不是系统产生的重复订单，确认后再调用确认付款接口或者在连连商户站后台操作疑似订单，api不调用确认付款接口
                    // 对于疑似重复订单，也可不做处理，
                    apiServiceResult.setCode(AssetBorrowOrder.STATUS_GO_SUBMIT);
                    apiServiceResult.setMsg("疑似重复订单");
                } else if (RetCodeEnum.isNeedQuery(paymentResponseBean.getRet_code())) {
                    // 出现1002，2005，4006，4007，4009，9999这6个返回码时（或者对除了0000之后的code都查询一遍查询接口）调用付款结果查询接口，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
                    apiServiceResult.setCode(AssetBorrowOrder.STATUS_SUB_SUBMIT);
                    apiServiceResult.setMsg("调用付款结果查询接口，明确订单状态");
                } else if(RetCodeEnum.isNoMoney(paymentResponseBean.getRet_code())){
                    apiServiceResult.setCode(AssetBorrowOrder.SUB_PAY_ERROR.toString());
                    apiServiceResult.setMsg("账户余额不足、限额");
                }else {
                    // 返回其他code时，可将订单置为失败
                    apiServiceResult.setCode(AssetBorrowOrder.SUB_PAY_ERROR.toString());
                    apiServiceResult.setMsg("代发失败");
                }
                //返回流水号
                apiServiceResult.setExt(noOrder);
            }
        } catch (Exception e) {
            apiServiceResult.setCode("500");
            apiServiceResult.setMsg("加密异常");
            logger.error("系统异常:处理连连实时返回结果>>>>>" + response);
        }
        return apiServiceResult;
    }

}
