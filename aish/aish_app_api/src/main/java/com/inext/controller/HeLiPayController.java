package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.BackConfigParams;
import com.inext.entity.heli.BindCardPayVo;
import com.inext.result.ApiResult;
import com.inext.result.ResponseDto;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IRepaymentService;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.helipay.*;
import com.inext.view.result.MapResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 合利宝
 */
@RestController
@RequestMapping(value = "/helipay")
public class HeLiPayController extends BaseController {
    private final static String HELIPAY_KEY ="HELIPAY_KEY:";

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HeLiPayController.class);

    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;

    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Resource
    IBorrowUserService borrowUserService;

    @Resource
    IRepaymentService iRepaymentService;

    @ApiOperation(value = "支付入口", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "还款类型(1取消订单，2续期)", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/userPay")
    public ApiResult<MapResult> userPay(HttpServletRequest request) {
        Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
        Map<String, String> params = getParameters(request);
        String orderIdStr=params.get("orderId");
        String typeStr=params.get("type");
        int orderId=0;
        int type=0;
        if(StringUtils.isNotEmpty(orderIdStr)&&StringUtils.isNotEmpty(typeStr)){
            orderId=Integer.parseInt(orderIdStr);
            type=Integer.parseInt(typeStr);
        }
       if(redisUtil.exists(HELIPAY_KEY +orderId)&&redisUtil.get(HELIPAY_KEY +orderId).toString().equalsIgnoreCase(typeStr)){//如果存在就 说明发起过 直接拒绝
            return new ApiResult("-1","您已发起此操作，请耐心等待！");
        }else if (type==1){
            AssetBorrowOrder order=iAssetBorrowOrderService.getOrderById(orderId);
            if (order.getStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
                return new ApiResult("-1","您已发起此操作，请耐心等待！");
            }
        }
        Map map=iRepaymentService.getRepaymentParams(userId,orderId,type,getIpAddr(request));
        return new ApiResult(map.get("code").toString(),map.get("message").toString(),new MapResult(map));
    }

    @ApiOperation(value = "获取短信", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "infoId", value = "infoId", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/sendValidateCode")
    public ApiResult<MapResult> sendValidateCode(HttpServletRequest request) {
        Map<String, String> params = getParameters(request);
        String infoId=params.get("infoId");
        String phone=borrowUserService.getBorrowUserById(this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId()).getCardPhone();


        SendValidateCodeVo vo=new SendValidateCodeVo();
        vo.setP1_bizType("QuickPaySendValidateCode");
        vo.setP2_customerNumber(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MCHNTCD"));
        vo.setP3_orderId(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY") +infoId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        vo.setP4_timestamp(sdf.format(new Date()));
        vo.setP5_phone(phone);
        Map json=new HashMap();
        try {
            Map<String, String> map = MyBeanUtils.convertBean(vo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null)+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MY_KEY") ;
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, "http://pay.trx.helipay.com/trx/quickPayApi/interface.action");
            log.info("响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SendValidateCodeResponseVo sendResponseVo = JSONObject.parseObject(resultMsg, SendValidateCodeResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(sendResponseVo, null)+Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DS_MY_KEY") ;
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = sendResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(sendResponseVo.getRt2_retCode())) {
                        json.put("code", "0");
                    } else {
                        json.put("code", "-1");
                    }
                    json.put("message", sendResponseVo.getRt3_retMsg());
                } else {
                    json.put("code", "-1");
                    json.put("message", "验签失败");
                }
            } else {
                json.put("code", "-1");
                json.put("message", "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", "-1");
            json.put("message", "请求异常：" + e.getMessage());
        }
        return new ApiResult<MapResult>(json.get("code").toString(),json.get("message").toString(),new MapResult(json));
    }

    @ApiOperation(value = "确认支付", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "infoId", value = "infoId", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "validateCode", value = "验证码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/confirmPay")
    public ApiResult<MapResult> confirmPay(HttpServletRequest request) {
        Map<String, String> params = getParameters(request);
        String infoId=params.get("infoId");
        String validateCode=params.get("validateCode");
        Map map=iRepaymentService.confirmPay(infoId,validateCode,getIpAddr(request));
        return new ApiResult(map.get("code").toString(),map.get("message").toString(),new MapResult(map));
    }

    @PostMapping("/callback")
    public String callback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getParameters(request);
        log.info("HeLiPayController.callback"+params.get("rt5_orderId")+":"+net.sf.json.JSONObject.fromObject(params));
        String responseCode =params.get("rt2_retCode");//响应代码
        if(StringUtils.isNotEmpty(responseCode)){
            params.put("RESPONSECODE",params.get("rt2_retCode"));
            params.put("RESPONSEMSG",params.get("rt3_retMsg"));
            params.put("MCHNTORDERID",params.get("rt5_orderId"));
            params.put("ORDERID",params.get("rt6_serialNumber"));
            params.put("AMT",params.get("rt8_orderAmount"));
            int isOk=iRepaymentService.doCcallback(params);
            if(isOk==1){
                return "success";
            }
        }
        return "fail";
    }
}
