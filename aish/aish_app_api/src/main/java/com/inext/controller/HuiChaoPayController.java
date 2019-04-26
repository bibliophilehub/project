package com.inext.controller;

import com.alibaba.fastjson.JSON;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.BackConfigParams;
import com.inext.result.ApiResult;
import com.inext.result.ResponseDto;
import com.inext.service.*;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.helipay.*;
import com.inext.utils.huichaopay.AliAppH5Demo;
import com.inext.utils.huichaopay.TestUtil;
import com.inext.view.result.MapResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 汇潮支付
 */
@RestController
@RequestMapping(value = "/huichaopay")
public class HuiChaoPayController extends BaseController {
    private final static String HUICHAOPAY_KEY ="HUICHAOPAY_KEY:";

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HuiChaoPayController.class);

    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Resource
    IBorrowUserService borrowUserService;

    @Resource
    IHcRepaymentService iHcRepaymentService;
    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;

    @ApiOperation(value = "汇潮支付入口", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型(1-安卓，2-ios)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "还款类型(1取消订单，2续期)", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/userPay")
    public ApiResult<MapResult> userPay(HttpServletRequest request) {
        Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
        Map<String, String> params = getParameters(request);
        String orderIdStr=params.get("orderId");//订单id
        String typeStr=params.get("type");//还款类型
        String deviceType=params.get("deviceType");//设备类型：1-Android，2-ios
        if(userId == null || StringUtils.isEmpty(orderIdStr)
                || StringUtils.isEmpty(typeStr) || StringUtils.isEmpty(deviceType)){
            return new ApiResult("-1","请求参数非法！");
        }
        int orderId=Integer.parseInt(orderIdStr);
        int type=Integer.parseInt(typeStr);
//       if(redisUtil.exists(HUICHAOPAY_KEY +orderId)
//               && redisUtil.get(HUICHAOPAY_KEY +orderId).toString().equalsIgnoreCase(typeStr)){
//            //如果存在就 说明发起过 直接拒绝
//            return new ApiResult("-1","您已发起此操作，请耐心等待！");
//        }else
        if (type == 1){
            AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
            if (order.getStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
                return new ApiResult("-1","您已发起此操作，请耐心等待！");
            }
        }
        //用户还款金额大小校验
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);
        if(assetRepaymentOrder != null
                && assetRepaymentOrder.getRepaymentAmount()!= null
                && assetRepaymentOrder.getRepaymentAmount().compareTo(new BigDecimal(3000)) > -1 ){
            return new ApiResult("-1","超过支付限额，请选择银行卡还款！");
        }
        Map map=iHcRepaymentService.getRepaymentParams(userId, orderId, type, deviceType);
        return new ApiResult(map.get("code").toString(),map.get("message").toString(),new MapResult(map));
    }

    /**
     * 汇潮回调
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/callback")
    public String callback(HttpServletRequest request, HttpServletResponse response) {
        log.info("---huichao----callback:"+request.getParameter("payResult"));
        //订单号
        String merchantOutOrderNo=request.getParameter("merchantOutOrderNo");
        //商户号
        String merid=request.getParameter("merid");
        //订单详情
        String msg=request.getParameter("msg");
        //随机字符串，和商户下单时传的一致
        String noncestr=request.getParameter("noncestr");
        //平台订单号
        String orderNo=request.getParameter("orderNo");
        //支付结果
        String payResult=request.getParameter("payResult");
        //签名
        String sign=request.getParameter("sign");
        //和下单时所填id字段一致，下单时未传则为空
        String id=request.getParameter("id");
        //支付宝订单号 支付宝渠道才会有
        String aliNo=request.getParameter("aliNo");
        //拼接签名参数
        Map<String, String> signParamMap=new HashMap<>();
        signParamMap.put("merchantOutOrderNo", merchantOutOrderNo);
        signParamMap.put("merid", merid);
        signParamMap.put("msg", msg);
        signParamMap.put("noncestr", noncestr);
        signParamMap.put("orderNo", orderNo);
        signParamMap.put("payResult", payResult);
        //转换为key=value模式
        String signParam= TestUtil.formatUrlMap(signParamMap, false, false);
        //生成签名
        String signLocal=TestUtil.getMD5(signParam + "&key="
                + Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HUICHAO).get("HUICHAO_KEY"));

        signParamMap.put("aliNo",aliNo);
        //回调参数
        log.info("HuiChaoPayController.callback:" + merchantOutOrderNo + ":" + JSONObject.fromObject(signParamMap));
        //对比签名
        if(signLocal.equals(sign)) {
            System.out.print("验签成功");
            //响应代码
            if(StringUtils.isNotEmpty(payResult)){
                int isOk=iHcRepaymentService.doCcallback(signParamMap);
                if(isOk==1){
                    return "success";
                }
            }
            return "success";
        }else {
            log.info("HuiChaoPayController.callback:验签失败"+merchantOutOrderNo+":" + JSONObject.fromObject(signParamMap));
            return "faild";
        }
    }

    /**
     * 汇潮代付回调
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/df/callback")
    public String daifu_callback(HttpServletRequest request, HttpServletResponse response) {
        log.info("---huichao----df---callback:" + request.getParameter("Result"));
        String merId = request.getParameter("MerNo");
        String amount = request.getParameter("Amount");
        String merBillNo = request.getParameter("MerBillNo");
        String cardNo = request.getParameter("CardNo");
        String result = request.getParameter("Result");
        String succeed = request.getParameter("Succeed");
        String signInfo = request.getParameter("SignInfo");
        Map<String, String> signParamMap=new HashMap<>();
        signParamMap.put("merId",merId);
        signParamMap.put("amount",amount);
        signParamMap.put("merBillNo",merBillNo);
        signParamMap.put("cardNo",cardNo);
        signParamMap.put("succeed",succeed);
        signParamMap.put("result",result);
        log.info("---huichao----df---callback回调参数：" + JSON.toJSONString(signParamMap));
        if(StringUtils.isEmpty(succeed)){
            return "failed";
        }else if(StringUtils.equals("00",succeed)){
            //成功
        }else if(StringUtils.equals("11",succeed)){
            //失败
        }else{

        }

        return "ok";
    }

}
