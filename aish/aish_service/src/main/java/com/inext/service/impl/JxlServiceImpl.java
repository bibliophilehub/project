package com.inext.service.impl;

import com.alibaba.fastjson.JSON;
import com.inext.dao.IRiskOrdersDao;
import com.inext.entity.BorrowUser;
import com.inext.entity.RiskOrders;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiServiceResult;
import com.inext.service.IJxlService;
import com.inext.utils.*;
import com.inext.view.result.JxlCollectResult;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * 类描述：芝麻信用相关接口 <br>
 * 每次发出请求之前插入订单信息，返回后更新订单信息<br>
 * 此类不做任何业务处理，仅拼接参数请求第三方，必须使用trycath，并且不向上抛出异常以保证插入或更新的订单不会回滚<br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-12-12 下午03:41:28 <br>
 */
@Service("jxlServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class JxlServiceImpl implements IJxlService {
    private final static String JXL_URL = "https://www.juxinli.com/";
    private final static String JXL_NAME = "lqkjxx";
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRiskOrdersDao riskOrdersDao;

    @Override
    public ApiServiceResult getToken(BorrowUser bu) {
        ApiServiceResult apiServiceResult = new ApiServiceResult();
        String result = "";
        String jxlUrl = JXL_URL + "orgApi/rest/v3/applications/" + JXL_NAME;
        RiskOrders orders = new RiskOrders();
        orders.setUserId(bu.getId() + "");
        orders.setOrderType(ConstantRisk.JXL);
        orders.setAct(ConstantRisk.GET_TOKEN);
        orders.setOrderNo(OrderNoUtil.getInstance().getUUID());
        logger.info("send to get user: " + bu.getUserName());
        String value = "{\"selected_website\":[],\"basic_info\":{\"name\"	:\"" + bu.getUserName() + "\",\"id_card_num\":\"" + bu.getUserCardNo()
                + "\",\"cell_phone_num\":\"" + bu.getUserPhone() + "\"},\"skip_mobile\":" + false + ",\"uid\":\"" + bu.getId() + "\",\"contacts\":[]}";
        orders.setReqParams(value);
        logger.info("jxl get token value:" + value);
        orders.setAddIp(RequestUtils.getIpAddr());
        riskOrdersDao.insertUseGeneratedKeys(orders);
        result = OkHttpUtils.post(jxlUrl, value);
        logger.info("getToken return" + result);
        orders.setReturnParams(result);
        orders.setStatus(RiskOrders.STATUS_SUC);
        riskOrdersDao.updateByPrimaryKey(orders);
        JSONObject jsonObject = JSONObject.fromObject(result);
        if ("65557".equals(jsonObject.getString("code"))) {
            jsonObject = jsonObject.getJSONObject("data");
            JSONObject jsonObject3 = jsonObject.getJSONObject("datasource");
            apiServiceResult = new ApiServiceResult(new JxlCollectResult(jsonObject.getString("token"), jsonObject3.getString("website")));
        } else if ("65545".equals(jsonObject.getString("code"))) {
            apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "紧急联系人手机号码不合法");
        } else {
            apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), jsonObject.getString("message"));
        }
        return apiServiceResult;
    }

    @Override
    public ApiServiceResult getCaptcha(HashMap<String, Object> params) {
        ApiServiceResult apiServiceResult = new ApiServiceResult("500", "未知异常，请稍后重试！");
        String result = "";
        Object userPhone = params.get("userPhone");
        Object website = params.get("website");
        Object password = params.get("password");
        Object queryPwd = params.get("queryPwd");
        Object userId = params.get("userId");
        Object token = params.get("token");

        String jxlUrl = JXL_URL + "orgApi/rest/v2/messages/collect/req";
        RiskOrders orders = new RiskOrders();
        String orderNo = OrderNoUtil.getInstance().getUUID();
        orders.setUserId(userId + "");
        orders.setOrderType(ConstantRisk.JXL);
        orders.setAct(ConstantRisk.APPLY_COLLECT);
        orders.setOrderNo(orderNo);
        orders.setReqParams(JSON.toJSONString(params));
        logger.info("send to get applyCollect " + orders.toString());
        String value = "{\"token\":\"" + token + "\",\"account\":\"" + userPhone + "\",\"password\":\"" + password + "\",\"website\":\""
                + website + "\"}";

        logger.info("jxl get applyCollect value:" + value);
        orders.setAddIp(RequestUtils.getIpAddr());
        riskOrdersDao.insertUseGeneratedKeys(orders);
        HashMap<String, Object> times = new HashMap<String, Object>();
        times.put("soketOut", 180000);
        times.put("connOut", 180000);
        result = WebClient.getInstance().postJsonData(jxlUrl, value, times);
        logger.info("get applyCollect result=" + result);
        orders.setReturnParams(result);
        orders.setStatus(RiskOrders.STATUS_SUC);
        riskOrdersDao.updateByPrimaryKey(orders);
        JSONObject jsonObject3 = JSONObject.fromObject(result);
        if (jsonObject3.getBoolean("success")) {
            JSONObject jsonObject2 = JSONObject.fromObject(result);
            JSONObject jsonObject = jsonObject2.getJSONObject("data");
            String code1 = jsonObject.getString("process_code");
            if ("10002".equals(code1)) {
                apiServiceResult = new ApiServiceResult("001", "短信已发送至您手机，请耐心等待并尽快输入验证码完成验证");
            } else if ("10008".equals(code1)) {
                apiServiceResult = new ApiServiceResult(ApiStatus.SUCCESS.getCode(), "开始采集数据!");
            } else if ("10022".equals(code1)) {
                String type = "";
                String queryPwdtype = "";
                if ("chinamobilebj".equals(website)) {
                    type = "\",\"type\":\"" + ConstantRisk.SUBMIT_QUERYPASSWORD;
                    queryPwdtype = "\",\"queryPwd\":\"" + queryPwd;
                }
                value = "{\"token\":\"" + token + "\",\"account\":\"" + userPhone + "\",\"password\":\"" + password + "\",\"website\":\""
                        + website + type + queryPwdtype + "\"}";
                result = WebClient.getInstance().postJsonData(jxlUrl, value, times);
                orders.setReturnParams(result);
                orders.setStatus(RiskOrders.STATUS_SUC);
                riskOrdersDao.updateByPrimaryKey(orders);
                JSONObject jsonObject4 = JSONObject.fromObject(result);
                if (jsonObject4.getBoolean("success")) {
                    JSONObject jsonObject6 = jsonObject2.getJSONObject("data");
                    String code2 = jsonObject6.getString("process_code");
                    if ("10002".equals(code2)) {
                        apiServiceResult = new ApiServiceResult("001", "短信已发送至您手机，请耐心等待并尽快输入验证码完成验证");
                    } else if ("10022".equals(code2)) {
                        apiServiceResult = new ApiServiceResult("002", "请输入查询密码");
                    } else if ("10008".equals(code2)) {
                        apiServiceResult = new ApiServiceResult(ApiStatus.SUCCESS.getCode(), "开始采集数据!");
                    } else {
                        apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), jsonObject3.getString("message") == null ? "未知异常" : jsonObject3
                                .getString("message"));
                    }
                }
            } else if ("RUNNING".equals(jsonObject.getString("type"))) {
                // 超时会出现该类型
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "操作超时,请重试");
            } else {
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), jsonObject.getString("content"));
            }

        } else {
            apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), jsonObject3.getString("message"));
        }
        return apiServiceResult;
    }

    /**
     * 提交验证码<br>
     * code为200时，msg是聚信立信息；<br>
     * 提交验证码请求，则开始采集数据
     */
    @Override
    public ApiServiceResult applyCollect(HashMap<String, Object> params) {
        ApiServiceResult apiServiceResult = new ApiServiceResult("500", "未知异常，请稍后重试！");
        String result = "";
        Object userPhone = params.get("userPhone");
        Object website = params.get("website");
        Object password = params.get("password");
        Object queryPwd = params.get("queryPwd");
        Object userId = params.get("userId");
        Object token = params.get("token");
        Object smsCaptcha = params.get("smsCaptcha");
        String jxlUrl = JXL_URL + "orgApi/rest/v2/messages/collect/req";
        RiskOrders orders = new RiskOrders();
        String orderNo = OrderNoUtil.getInstance().getUUID();
        orders.setUserId(userId + "");
        orders.setOrderType(ConstantRisk.JXL);
        orders.setAct(ConstantRisk.APPLY_COLLECT);
        orders.setOrderNo(orderNo);
        orders.setReqParams(JSON.toJSONString(params));
        logger.info("send to get applyCollect " + orders.toString());
        String cap = "";
        String type = "";
        if (smsCaptcha != null && StringUtils.isNotBlank(smsCaptcha.toString())) {
            if ("chinamobilebj".equals(website)) {
                if (StringUtils.isNotEmpty(queryPwd + "")) {
                    cap = "\",\"type\":\"" + ConstantRisk.SUBMIT_QUERYPASSWORD + "\",\"captcha\":\"" + smsCaptcha + "\",\"queryPwd\":\""
                            + queryPwd;
                } else {
                    type = "SUBMIT_CAPTCHA";
                    cap = "\",\"captcha\":\"" + smsCaptcha + "\",\"type\":\"" + type;
                }
            } else {
                type = "SUBMIT_CAPTCHA";
                cap = "\",\"captcha\":\"" + smsCaptcha + "\",\"type\":\"" + type;
            }
        }
        String value = "{\"token\":\"" + token + "\",\"account\":\"" + userPhone + "\",\"password\":\"" + password + "\",\"website\":\""
                + website + cap + "\"}";

        orders.setAddIp(RequestUtils.getIpAddr());
        riskOrdersDao.insertUseGeneratedKeys(orders);
        HashMap<String, Object> times = new HashMap<String, Object>();
        times.put("soketOut", 180000);
        times.put("connOut", 180000);
        result = WebClient.getInstance().postJsonData(jxlUrl, value, times);
        logger.info("get applyCollect result=" + result);
        orders.setReturnParams(result);
        orders.setStatus(RiskOrders.STATUS_SUC);
        riskOrdersDao.updateByPrimaryKey(orders);
        JSONObject jsonObject3 = JSONObject.fromObject(result);
        if (jsonObject3.getBoolean("success")) {
            JSONObject jsonObject2 = JSONObject.fromObject(result);
            JSONObject jsonObject = jsonObject2.getJSONObject("data");
            String code1 = jsonObject.getString("process_code");
            if ("10002".equals(code1)) {

                apiServiceResult = new ApiServiceResult("001", "短信已发送至您手机，请耐心等待并尽快输入验证码完成验证");
            } else if ("10022".equals(code1)) {
                apiServiceResult = new ApiServiceResult("002", "请输入查询密码");
            } else if ("10008".equals(code1)) {
                apiServiceResult = new ApiServiceResult(ApiStatus.SUCCESS.getCode(), "开始采集数据!");
            } else if ("10006".equals(code1)) {
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), jsonObject.getString("content"));
            } else if ("10004".equals(code1)) {
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "密码错误!");
            } else {
                apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), "其他异常！");
            }
        } else {
            apiServiceResult = new ApiServiceResult(ApiStatus.FAIL.getCode(), jsonObject3.getString("message"));
        }
        return apiServiceResult;
    }

}