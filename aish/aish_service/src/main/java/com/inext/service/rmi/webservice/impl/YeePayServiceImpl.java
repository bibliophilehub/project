
package com.inext.service.rmi.webservice.impl;

import com.alibaba.fastjson.JSON;
import com.inext.configuration.YeePayConfig;
import com.inext.constants.Constants;
import com.inext.entity.*;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.DKStatus;
import com.inext.enums.PaymentTypeEnum;
import com.inext.enums.YOPStatus;
import com.inext.result.ApiServiceResult;
import com.inext.service.*;
import com.inext.service.rmi.webservice.YeePayService;
import com.inext.util.YeepayServiceUtil;
import com.inext.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class YeePayServiceImpl implements YeePayService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(YeePayServiceImpl.class);

    @Autowired
    IBorrowUserService borrowUserService;
    @Autowired
    RepaymentOrderDaikouService repaymentOrderDaikouService;
    @Autowired
    IAssetRepaymentOrderService repaymentOrderService;
    @Autowired
    IAssetBorrowOrderService borrowOrderService;
    @Autowired
    IAssetOrderStatusHisService orderStatusHisService;
    @Autowired
    IAssetRepaymentOrderDetailService repaymentOrderDetailService;
//    @Autowired
//    IComboUserSerivce comboUserSerivce;

    //易宝绑卡鉴权接口
    String authbindcardreqUri = YeePayConfig.getInstance().getValue("authbindcardreqUri");
    //易宝绑卡鉴权短验
    String authbindcardconfirmUri = YeePayConfig.getInstance().getValue("authbindcardconfirmUri");
    //易宝首次支付，如果是商户发起则是代扣
    String unionfirstpayUri = YeePayConfig.getInstance().getValue("unionfirstpayUri");
    //易宝首次支付查询接口
    String firstpayqueryUri = YeePayConfig.getInstance().getValue("firstpayqueryUri");
    //易宝绑卡支付
    String unibindcardpayUri = YeePayConfig.getInstance().getValue("unibindcardpayUri");
    //易宝绑卡支付查询
    String bindcardpayqueryUri = YeePayConfig.getInstance().getValue("bindcardpayqueryUri");

    @Override
    public ApiServiceResult<Object> authBindCard(BorrowUser borrowUser) throws IOException
    {
        //商户编号
        String merchantno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("MERCHANT_NO"));
        //还款请求号 我方生成的唯一支付请求号
        String requestno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("BIND_CARD_KEY") + borrowUser.getId() + "_" + System.currentTimeMillis());
        //用户标识 商户生成的用户唯一标识
        String identityid = format(borrowUser.getId() + "");
        //用户标识类型  MAC：网卡地址、IMEI：国际移动设备标识、ID_CARD：用户身份证号、PHONE：手机号、EMAIL：邮箱、USER_ID：用户 id、AGREEMENT_NO：用户纸质订单协议号
        String identitytype = format("ID_CARD");
        //卡号
        String cardno = format(borrowUser.getCardCode());
        //证件号
        String idcardno = format(borrowUser.getUserCardNo());
        //证件类型
        String idcardtype = format("ID");
        //用户姓名
        String username = format(borrowUser.getUserName());
        //手机号
        String phone = format(borrowUser.getCardPhone());
        //是否发短信 true:有短验 、false:无短验
        String issms = format("false");
        //建议短验发送类型 非必填参数
        String advicesmstype = format("");
        //定制短验模板 非必填参数
        String smstemplateid = format("");
        //短验模板 非必填参数
        String smstempldatemsg = format("");
        // 绑卡订单有效期 单位：分钟  若不传则默认有效期 24h
        String avaliabletime = format("");
        //回调地址 Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("BIND_CARD_URL")
        String callbackurl = format("");
        //请求时间 格式：yyyy-MM-dd HH:mm:ss
        String requesttime = format(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //鉴权类型 COMMON_FOUR(验四)
        String authtype = format("COMMON_FOUR");
        //备注 非必填参数
        String remark = format("");
        //扩展信息 非必填参数
        String extinfos = format("");

        Map<String, String> map = new HashMap<String, String>();
        map.put("merchantno", merchantno);
        map.put("requestno", requestno);
        map.put("identityid", identityid);
        map.put("identitytype", identitytype);
        map.put("cardno", cardno);
        map.put("idcardno", idcardno);
        map.put("idcardtype", idcardtype);
        map.put("username", username);
        map.put("phone", phone);
        map.put("issms", issms);
        map.put("advicesmstype", advicesmstype);
        map.put("smstemplateid", smstemplateid);
        map.put("smstempldatemsg", smstempldatemsg);
        map.put("avaliabletime", avaliabletime);
        map.put("callbackurl", callbackurl);
        map.put("requesttime", requesttime);
        map.put("authtype", authtype);
        map.put("remark", remark);
        map.put("extinfos", extinfos);
        Map<String, String> yopresponsemap = YeepayServiceUtil.yeepayYOP(map, authbindcardreqUri);
        if (yopresponsemap == null)
        {
            LOGGER.info("易宝代扣鉴权失败：对方返回为空");
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), ApiStatus.FAIL.getValue());
        }

        LOGGER.info("易宝代扣鉴权绑卡返回：{}", JSON.toJSONString(yopresponsemap));
        String responseStatus = yopresponsemap.get("status");
        if (!YOPStatus.BIND_SUCCESS.getValue().equals(responseStatus))
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), ApiStatus.FAIL.getValue());
        }
        return new ApiServiceResult<Object>(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getValue());
    }

    public static String format(String text)
    {
        return text == null ? "" : text.trim();
    }

    @Override
    public Map<String, String> userBindCard(Map<String, String> bindParaMap) throws IOException
    {
        String userId = bindParaMap.get("userId");
        String cardNo = bindParaMap.get("cardNo");//银行卡号码
        String idNo = bindParaMap.get("idNo");//身份证
        String userName = bindParaMap.get("actName");// 真实姓名
        String mobilePhone = bindParaMap.get("phone"); //预留手机号

        //商户编号
        String merchantno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("MERCHANT_NO"));
        //还款请求号 我方生成的唯一支付请求号
        String requestno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("BIND_CARD_KEY") + userId + "_" + System.currentTimeMillis());
        //用户标识 商户生成的用户唯一标识
        String identityid = format(userId + "");
        //用户标识类型  MAC：网卡地址、IMEI：国际移动设备标识、ID_CARD：用户身份证号、PHONE：手机号、EMAIL：邮箱、USER_ID：用户 id、AGREEMENT_NO：用户纸质订单协议号
        String identitytype = format("ID_CARD");
        //卡号
        String cardno = format(cardNo);
        //证件号
        String idcardno = format(idNo);
        //证件类型
        String idcardtype = format("ID");
        //用户姓名
        String username = format(userName);
        //手机号
        String phone = format(mobilePhone);
        //是否发短信 true:有短验 、false:无短验
        String issms = format("true");
        //建议短验发送类型 非必填参数
        String advicesmstype = format("MESSAGE");
        //定制短验模板 非必填参数
        String smstemplateid = format("");
        //短验模板 非必填参数
        String smstempldatemsg = format("");
        // 绑卡订单有效期 单位：分钟  若不传则默认有效期 24h
        String avaliabletime = format("");
        //回调地址 Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("BIND_CARD_URL")
        String callbackurl = format("");
        //请求时间 格式：yyyy-MM-dd HH:mm:ss
        String requesttime = format(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //鉴权类型 COMMON_FOUR(验四)
        String authtype = format("COMMON_FOUR");
        //备注 非必填参数
        String remark = format("");
        //扩展信息 非必填参数
        String extinfos = format("");

        Map<String, String> map = new HashMap<String, String>();
        map.put("merchantno", merchantno);
        map.put("requestno", requestno);
        map.put("identityid", identityid);
        map.put("identitytype", identitytype);
        map.put("cardno", cardno);
        map.put("idcardno", idcardno);
        map.put("idcardtype", idcardtype);
        map.put("username", username);
        map.put("phone", phone);
        map.put("issms", issms);
        map.put("advicesmstype", advicesmstype);
        map.put("smstemplateid", smstemplateid);
        map.put("smstempldatemsg", smstempldatemsg);
        map.put("avaliabletime", avaliabletime);
        map.put("callbackurl", callbackurl);
        map.put("requesttime", requesttime);
        map.put("authtype", authtype);
        map.put("remark", remark);
        map.put("extinfos", extinfos);

        Map<String, String> result = new HashMap<>();
        Map<String, String> yopresponsemap = YeepayServiceUtil.yeepayYOP(map, authbindcardreqUri);
        if (yopresponsemap == null)
        {
            result.put("code", ApiStatus.FAIL.getCode());
            result.put("msg", ApiStatus.FAIL.getValue());
            LOGGER.info("易宝代扣鉴权失败：对方返回为空");
            return result;
        }

        LOGGER.info("易宝代扣鉴权绑卡返回：{}", JSON.toJSONString(yopresponsemap));
        String responseStatus = yopresponsemap.get("status");

        if (!YOPStatus.TO_VALIDATE.getValue().equals(responseStatus))
        {
            result.put("code", ApiStatus.FAIL.getCode());
            result.put("msg", yopresponsemap.get("errormsg"));
            return result;
        }

        result.put("code", ApiStatus.SUCCESS.getCode());
        result.put("msg", ApiStatus.SUCCESS.getValue());
        result.put("requestno", yopresponsemap.get("requestno"));
        result.put("smscode", yopresponsemap.get("smscode"));
        result.put("codesender", yopresponsemap.get("codesender"));//短信发送方
        result.put("smstype", yopresponsemap.get("smstype"));
        return result;
    }

    @Override
    public ApiServiceResult<Object> autoFirstPay(AssetRepaymentOrder repayment) throws IOException
    {
        BorrowUser borrowUser = borrowUserService.getBorrowUserById(repayment.getUserId());
        if (borrowUser == null)
        {
            LOGGER.info("还款订单号：{}的用户信息不存在", repayment.getId());
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "该笔订单用户信息不存在");
        }
        // 用户应还金额 = 应还总金额 - 已还金额 - 减免金额
        BigDecimal shouldRepayMoney = repayment.getRepaymentAmount().subtract(repayment.getRepaymentedAmount()).subtract(repayment.getCeditAmount());
        AssetRepaymentOrderDaikou initDaikou = new AssetRepaymentOrderDaikou();
        initDaikou.setRepaymentOrderId(repayment.getId());
        initDaikou.setReqAmt(shouldRepayMoney);
        initDaikou.setPayBussiness(1);
        repaymentOrderDaikouService.insertSelective(initDaikou);

        Map<String, String> yopresponsemap = null;
        try
        {
            //商户编号
            String merchantno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("MERCHANT_NO"));
            //还款请求号
            String requestno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_KEY") + initDaikou.getId());
            //是否发短信 true:有短验 、false:无短验
            String issms = format("false");
            //用户标识
            String identityid = format(repayment.getUserId() + "");
            //用户标识类型  MAC：网卡地址、IMEI：国际移动设备标识、ID_CARD：用户身份证号、PHONE：手机号、EMAIL：邮箱、USER_ID：用户 id、AGREEMENT_NO：用户纸质订单协议号
            String identitytype = format("ID_CARD");
            //卡号前六位
            String cardtop = format(borrowUser.getCardCode().substring(0, 6));
            //卡号后四位
            String cardlast = format(borrowUser.getCardCode().substring(borrowUser.getCardCode().length() - 4));
            //还款金额 
            String amount = format(shouldRepayMoney.toString());
            //建议短验发送类型 非必填参数
            String advicesmstype = format("");
            // 绑卡订单有效期 单位：分钟  若不传则默认有效期 24h
            String avaliabletime = format("");
            //商品名称
            String productname = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("YOP_PRODUCT_NAME"));
            //回调地址 Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_URL")
            String callbackurl = format("");
            //请求时间 格式：yyyy-MM-dd HH:mm:ss
            String requesttime = format(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            //终端号 快捷：SQKKSCENEKJ010     代扣：SQKKSCENE10
            String terminalno = format("SQKKSCENEKJ010");
            //备注 非必填参数
            String remark = format("");
            //扩展信息 非必填参数
            String extinfos = format("");
            //分账结果通知地  非必填参数 不分账不要传这个参数
            String dividecallbackurl = format("");
            //分账信息 非必填参数
            String dividejstr = format("");

            Map<String, String> map = new HashMap<String, String>();
            map.put("merchantno", merchantno);
            map.put("requestno", requestno);
            map.put("issms", issms);
            map.put("identityid", identityid);
            map.put("identitytype", identitytype);
            map.put("cardtop", cardtop);
            map.put("cardlast", cardlast);
            map.put("amount", amount);
            map.put("advicesmstype", advicesmstype);
            map.put("avaliabletime", avaliabletime);
            map.put("productname", productname);
            map.put("callbackurl", callbackurl);
            map.put("requesttime", requesttime);
            map.put("terminalno", terminalno);
            map.put("remark", remark);
            map.put("extinfos", extinfos);
            map.put("dividecallbackurl", dividecallbackurl);
            map.put("dividejstr", dividejstr);

            LOGGER.info("用户手机号:{},易宝代扣请求参数{}", borrowUser.getUserPhone(), JSON.toJSONString(map));

            yopresponsemap = YeepayServiceUtil.yeepayYOP(map, unibindcardpayUri);
        }
        catch (Exception e)
        {
            LOGGER.error("易宝代扣参数异常:" + e.getMessage(), e);
        }
        if (yopresponsemap == null)
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "易宝返回为空");
        }

        LOGGER.info("易宝代扣返回结果：{}", JSON.toJSONString(yopresponsemap));

        if (isEmpty(yopresponsemap.get("status")))
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "易宝返回报错");
        }
        //易宝返回状态
        String chargeStatus = yopresponsemap.get("status");

        if (chargeStatus.equals(YOPStatus.FAIL.getValue()))
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "系统异常");
        }
        else if (chargeStatus.equals(YOPStatus.TIME_OUT.getValue()))
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "连接易宝超时");
        }
        else if (chargeStatus.equals(YOPStatus.BIND_ERROR.getValue()))
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "易宝绑卡异常");
        }
        else if (chargeStatus.equals(YOPStatus.BIND_FAIL.getValue()))
        {
            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "易宝绑卡失败");
        }
        //首次还款请求号
        String responseNo = yopresponsemap.get("requestno");
        //易宝流水号
        String yborderId = yopresponsemap.get("yborderid");
        String repaymentDaikouId = responseNo.substring(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_KEY").length());
        AssetRepaymentOrderDaikou daiKouRecord = new AssetRepaymentOrderDaikou();
        daiKouRecord.setId(Integer.parseInt(repaymentDaikouId));
        daiKouRecord.setChargeStatus(chargeStatus);
        daiKouRecord.setPayOrderId(responseNo);
        daiKouRecord.setStatus(DKStatus.DOING.getValue());
        daiKouRecord.setUpdateTime(new Date());
        daiKouRecord.setDkOrderId(yborderId);
        repaymentOrderDaikouService.updateByPrimaryKeySelective(daiKouRecord);

        //更新当前还款表数据,将还款表中代扣还款支付状态改为1 处理中
        AssetRepaymentOrder updateRepayment = new AssetRepaymentOrder();
        updateRepayment.setDkPayStatus(AssetRepaymentOrder.DK_PAY_DOING);
        updateRepayment.setId(repayment.getId());
        repaymentOrderService.update(updateRepayment);

        return new ApiServiceResult<Object>(ApiStatus.SUCCESS.getCode(), "易宝代扣请求成功");
    }

    /**
     * 
     * TODO 易宝首次支付接口
     * @author wxy
     * @date 2018年6月29日
     * @param repayment
     * @param borrowUser
     * @param shouldRepayMoney
     * @param initDaikou
     * @return
     */
    @SuppressWarnings("unused")
    @Deprecated
    private Map<String, String> yeeFistPay(AssetRepaymentOrder repayment, BorrowUser borrowUser, BigDecimal shouldRepayMoney, AssetRepaymentOrderDaikou initDaikou)
    {
        //商户编号
        String merchantno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("MERCHANT_NO"));
        //还款请求号
        String requestno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_KEY") + initDaikou.getId());
        //用户标识
        String identityid = format(repayment.getUserId() + "");
        //用户标识类型  MAC：网卡地址、IMEI：国际移动设备标识、ID_CARD：用户身份证号、PHONE：手机号、EMAIL：邮箱、USER_ID：用户 id、AGREEMENT_NO：用户纸质订单协议号
        String identitytype = format("ID_CARD");
        //卡号
        String cardno = format(borrowUser.getCardCode());
        //证件号
        String idcardno = format(borrowUser.getUserCardNo());
        //证件类型
        String idcardtype = format("ID");
        //用户姓名
        String username = format(borrowUser.getUserName());
        //手机号
        String phone = format(borrowUser.getCardPhone());
        //还款金额
        String amount = format(shouldRepayMoney.toString());
        //鉴权类型
        String authtype = format("COMMON_FOUR");
        //是否发短信 true:有短验 、false:无短验
        String issms = format("true");
        //建议短验发送类型 非必填参数
        String advicesmstype = format("");
        //定制短验模板 非必填参数
        String smstemplateid = format("");
        //短验模板 非必填参数
        String smstempldatemsg = format("");
        // 绑卡订单有效期 单位：分钟  若不传则默认有效期 24h
        String avaliabletime = format("");
        //回调地址 Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_URL")
        String callbackurl = format("");
        //请求时间 格式：yyyy-MM-dd HH:mm:ss
        String requesttime = format(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //商品名称 非必填参数
        String productname = format("");
        //终端号 快捷：SQKKSCENEKJ010     代扣：SQKKSCENE10
        String terminalno = format("SQKKSCENEKJ010");
        //分账结果通知地  非必填参数 不分账不要传这个参数
        String dividecallbackurl = format("");
        //分账信息 非必填参数
        String newdividejstr = format("");
        Map<String, String> map = new HashMap<String, String>();
        map.put("merchantno", merchantno);
        map.put("requestno", requestno);
        map.put("identityid", identityid);
        map.put("identitytype", identitytype);
        map.put("cardno", cardno);
        map.put("idcardno", idcardno);
        map.put("idcardtype", idcardtype);
        map.put("username", username);
        map.put("phone", phone);
        map.put("amount", amount);
        map.put("authtype", authtype);
        map.put("issms", issms);
        map.put("advicesmstype", advicesmstype);
        map.put("smstempldatemsg", smstempldatemsg);
        map.put("smstemplateid", smstemplateid);
        map.put("avaliabletime", avaliabletime);
        map.put("callbackurl", callbackurl);
        map.put("requesttime", requesttime);
        map.put("productname", productname);
        map.put("terminalno", terminalno);
        map.put("dividecallbackurl", dividecallbackurl);
        map.put("newdividejstr", newdividejstr);
        return map;
    }

    @Override
    public ApiServiceResult<Object> queryFirstPay(AssetRepaymentOrderDaikou daiKou) throws IOException
    {
        //商户编号
        String merchantno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("MERCHANT_NO"));
        //还款请求号 Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_KEY") + daiKou.getId()
        String requestno = format(daiKou.getPayOrderId());
        String yborderid = format(daiKou.getDkOrderId());

        Map<String, String> map = new HashMap<String, String>();
        map.put("merchantno", merchantno);
        map.put("requestno", requestno);
        map.put("yborderid", yborderid);
        Map<String, String> yopresponsemap = YeepayServiceUtil.yeepayYOP(map, bindcardpayqueryUri);
        if (yopresponsemap == null)
        {
            return new ApiServiceResult<Object>(ApiStatus.DEALING.getCode(), "易宝返回为空");
        }

        LOGGER.info("易宝代扣返回结果：{}", JSON.toJSONString(yopresponsemap));
        if (isEmpty(yopresponsemap.get("status")))
        {
            return new ApiServiceResult<Object>(ApiStatus.DEALING.getCode(), "易宝返回错误");
        }
        //易宝返回状态
        String chargeStatus = yopresponsemap.get("status");
        //代扣方扣款金额
        String amount = yopresponsemap.get("amount");
        //首次还款请求号
        String responseNo = yopresponsemap.get("requestno");
        String repaymentDaikouId = responseNo.substring(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("DK_KEY").length());
        //代扣还款记录表
        AssetRepaymentOrderDaikou dk = repaymentOrderDaikouService.selectByPrimaryKey(Integer.parseInt(repaymentDaikouId));
        //还款表
        AssetRepaymentOrder repaymentOrder = repaymentOrderService.selectByPrimaryKey(dk.getRepaymentOrderId());
        //借款表
        AssetBorrowOrder borrowOrder = borrowOrderService.getOrderById(repaymentOrder.getOrderId());
        //易宝代扣成功
        if (chargeStatus.equals(YOPStatus.PAY_SUCCESS.getValue()))
        {
            //更新代扣表数据
            AssetRepaymentOrderDaikou daiKouResult = new AssetRepaymentOrderDaikou();
            daiKouResult.setId(Integer.parseInt(repaymentDaikouId));
            daiKouResult.setResAmt(new BigDecimal(amount));
            daiKouResult.setStatus(DKStatus.SUCCESS.getValue());
            daiKouResult.setChargeStatus(chargeStatus);
            daiKouResult.setUpdateTime(new Date());
            repaymentOrderDaikouService.updateByPrimaryKeySelective(daiKouResult);

            //还款状态
            Integer status = AssetOrderStatusHis.STATUS_YHK;
            //TODO 重写还款逻辑
//            if (repaymentOrder.getLateDay() > 0)
//            {
//                status = AssetOrderStatusHis.STATUS_YQHQ;
//            }
            // SmsSendUtil.sendSmsDiy(borrowOrder.getUserPhone(), Constants.SIGN + "您申请的手机回购订单已结束，小猪优品乐无穷，期待您下次手机回购之旅。");

            //重写 发短信逻辑
//            String tempateCode = "sms_xjyp_1121";
//            SmsUtil.sendSmsMessage(borrowOrder.getUserPhone(), tempateCode, "", BackConfigParams.SMS_NORMAL);

            //还款成功记录
            AssetOrderStatusHis his = new AssetOrderStatusHis();

            his.setOrderId(borrowOrder.getId());
            his.setOrderStatus(status);
//            if (repaymentOrder.getLateDay() > 0)
//            {
//                his.setRemark("已取消交易，返还预付款+违约金+滞纳金，共" + new BigDecimal(amount) + "元");
//            }
//            else
//            {
//                his.setRemark("已取消交易，返还预付款+违约金，共" + new BigDecimal(amount) + "元");
//            }
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
            if (isEmpty(repaymentOrder.getNoOrder()))
            {
                updateRepaymentOrder.setNoOrder(responseNo);
            }
            else
            {
                if (!repaymentOrder.getNoOrder().contains(responseNo))
                {
                    updateRepaymentOrder.setNoOrder(repaymentOrder.getNoOrder() + "," + responseNo);
                }
            }
            updateRepaymentOrder.setUpdateTime(now);
            updateRepaymentOrder.setRepaymentRealTime(now);
            Integer repaymentType = PaymentTypeEnum.YOP_DK.getValue();
            updateRepaymentOrder.setRepaymentType(repaymentType);
            //三方支付成功状态
            updateRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_SUCCESS);
            //上次已还款金额
            BigDecimal lastRepaymentedAmount = repaymentOrder.getRepaymentedAmount();

            LOGGER.info("易宝代扣扣款---AssetRepaymentOrder--订单号为{},还款状态为：{},上次已还款金额：{}元，本次还款：{}元", repaymentOrder.getId(), status, lastRepaymentedAmount, new BigDecimal(amount));

            //本次还款金额
            updateRepaymentOrder.setRepaymentedAmount(new BigDecimal(amount));
            updateRepaymentOrder.setUpdateTime(new Date());

            repaymentOrderService.update(updateRepaymentOrder);

            // 2018-07-02 当用户成功还款后 将用户标识更新为老用户  那么当用户新创建一笔订单时会使用到这个字段标识该笔订单信息
            BorrowUser user = new BorrowUser();
            user.setId(borrowOrder.getUserId());
            user.setIsOld(1);
            borrowUserService.updateUser(user);

            /*
             *增加还款流水详情
             */
            repaymentOrderDetailService.insertAssetRepaymentOrderDetail(new BigDecimal(amount), new BigDecimal(0), repaymentOrder, status, "易宝代扣", repaymentType, responseNo, null);

            /**
             * 用户提额
             */
            //TODO 提额重写
            //comboUserSerivce.promotionCombo(repaymentOrder.getUserId());

            return new ApiServiceResult<Object>(ApiStatus.SUCCESS.getCode(), "易宝代扣成功");
        }
        else if (chargeStatus.equals(YOPStatus.PAY_FAIL.getValue()) || chargeStatus.equals(YOPStatus.TIME_OUT.getValue()))//代扣失败或超时失效
        {
            AssetRepaymentOrderDaikou daiKouResult = new AssetRepaymentOrderDaikou();
            daiKouResult.setId(Integer.parseInt(repaymentDaikouId));
            daiKouResult.setResAmt(new BigDecimal(amount));
            daiKouResult.setStatus(DKStatus.FAIL.getValue());
            daiKouResult.setChargeStatus(chargeStatus);
            daiKouResult.setUpdateTime(new Date());
            repaymentOrderDaikouService.updateByPrimaryKeySelective(daiKouResult);

            AssetRepaymentOrder updateRepaymentOrder = new AssetRepaymentOrder();
            updateRepaymentOrder.setId(repaymentOrder.getId());
            updateRepaymentOrder.setDkPayStatus(AssetRepaymentOrder.DK_PAY_FAILED);
            updateRepaymentOrder.setUpdateTime(new Date());
            repaymentOrderService.update(updateRepaymentOrder);

            return new ApiServiceResult<Object>(ApiStatus.FAIL.getCode(), "易宝代扣失败");
        }
        else
        {
            return new ApiServiceResult<Object>(ApiStatus.DEALING.getCode(), "易宝代扣处理中");
        }
    }

    @Override
    public Map<String, String> userBindCardConfrim(Map<String, String> bindParaMap) throws IOException
    {
        String requestno = format(bindParaMap.get("requestno"));// 用户鉴权绑卡请求订单号
        String validatecode = format(bindParaMap.get("verifyCode")); //对方短信内容

        String merchantno = format(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("MERCHANT_NO"));
        Map<String, String> map = new HashMap<String, String>();
        map.put("merchantno", merchantno);
        map.put("requestno", requestno);
        map.put("validatecode", validatecode);

        Map<String, String> result = new HashMap<>();
        Map<String, String> yopresponsemap = YeepayServiceUtil.yeepayYOP(map, authbindcardconfirmUri);

        if (yopresponsemap == null)
        {
            result.put("code", ApiStatus.FAIL.getCode());
            result.put("msg", ApiStatus.FAIL.getValue());
            LOGGER.info("易宝代扣鉴权确定失败：对方返回为空");
            return result;
        }

        LOGGER.info("易宝代扣鉴权确定返回：{}", JSON.toJSONString(yopresponsemap));
        if (isEmpty(yopresponsemap.get("status")))
        {
            result.put("code", ApiStatus.FAIL.getCode());
            result.put("msg", ApiStatus.FAIL.getValue());
            LOGGER.info("易宝代扣鉴权确定失败：对方返回错误");
            return result;
        }

        String responseStatus = yopresponsemap.get("status");

        if (!YOPStatus.BIND_SUCCESS.getValue().equals(responseStatus))
        {
            result.put("code", ApiStatus.FAIL.getCode());
            result.put("msg", yopresponsemap.get("errormsg"));
            result.put("errorcode", yopresponsemap.get("errorcode"));
            return result;
        }
        result.put("code", ApiStatus.SUCCESS.getCode());
        result.put("msg", YOPStatus.BIND_SUCCESS.getDesc());
        return result;
    }
}
