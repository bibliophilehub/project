package com.inext.controller;

import com.inext.constants.RedisCacheConstants;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiResult;
import com.inext.result.ResponseDto;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IRepaymentService;
import com.inext.utils.JsonUtils;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.newbaofoo.rsa.SecurityUtil;
import com.inext.utils.newbaofoo.rsa.SignatureUtils;
import com.inext.utils.newbaofoo.util.FormatUtil;
import com.inext.view.result.MapResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping(value = "/baoFooPay")
public class BaoFooPayApiController extends BaseController {

    private final static String BAOFOOPAY_KEY="BAOFOOPAY_KEY:";

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RelyController.class);
    @Resource
    IRepaymentService iRepaymentService;

    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;

    @Resource(name = "redisUtil")
    RedisUtil redisUtil;

    @ApiOperation(value = "宝付主动支付", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "type", value = "类型：1-还款，2-续期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/singlePay")
    public ApiResult<MapResult> confirmPay(HttpServletRequest request) throws Exception {
        Map<String, String> params = getParameters(request);
        Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
        String orderIdStr=params.get("orderId");
        String typeStr=params.get("type");
        int orderId=0;
        int type=0;
        if(StringUtils.isNotEmpty(orderIdStr)&&StringUtils.isNotEmpty(typeStr)){
            orderId=NumberUtils.toInt(orderIdStr);
            type=NumberUtils.toInt(typeStr);
        }
        if(redisUtil.exists(BAOFOOPAY_KEY+orderId)&&redisUtil.get(BAOFOOPAY_KEY+orderId).toString().equalsIgnoreCase(typeStr)){//如果存在就 说明发起过 直接拒绝
            return new ApiResult(ApiStatus.FAIL.getCode(), "您已发起此操作，请耐心等待！");
        }else if (type==1){
            AssetBorrowOrder order=iAssetBorrowOrderService.getOrderById(orderId);
            if (order.getStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
                return new ApiResult(ApiStatus.FAIL.getCode(), "您的订单已完成，无需重复还款");
            }
        }

        redisUtil.set(BAOFOOPAY_KEY+orderId, typeStr,60*3L);

        Map map=iRepaymentService.baoFooConfirmPay(userId, orderId, type, 0);
        return new ApiResult(map.get("code").toString(),map.get("message").toString(),new MapResult(map));
    }

    /**
     * 宝付直接支付（协议支付）回调
     * @throws Exception
     */
    @PostMapping("/singlePay/callback")
    public String singlePayCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");//utf-8编码接收
//        logger.getAllParamLog(request);
        Map<String,String> RDateArry = new TreeMap<String,String>();

        Enumeration<String> e = request.getParameterNames();
        while(e.hasMoreElements()) {
            String paraStr = e.nextElement();
            if(paraStr != null){
                RDateArry.put(paraStr, request.getParameter(paraStr));//接收参数,（使用TreeMap自动排序）
            }
        }

        logger.info("----------宝付主动还款-------回调-------结果：" + JsonUtils.beanToJson(RDateArry));

        String cerpath = PropertiesUtil.get("baofoo.rzzf.pub.key");//宝付公钥

        if(!RDateArry.containsKey("signature")){
            throw new Exception("缺少验签参数！");
        }
        String RSign=RDateArry.get("signature");
        logger.info("----------宝付主动还款-------回调-------返回的验签值："+RSign);
        RDateArry.remove("signature");//需要删除签名字段
        String RSignVStr = FormatUtil.coverMap2String(RDateArry);
        logger.info("----------宝付主动还款-------回调-------返回SHA-1摘要字串："+RSignVStr);
        String RSignature = SecurityUtil.sha1X16(RSignVStr, "UTF-8");//签名
        logger.info("----------宝付主动还款-------回调-------返回SHA-1摘要结果："+RSignature);

        if(SignatureUtils.verifySignature(cerpath,RSignature,RSign)){
            logger.info("Yes");//验签成功
        }
        if(!RDateArry.containsKey("resp_code")){
            throw new Exception("缺少resp_code参数！");
        }
        if(RDateArry.get("resp_code").toString().equals("S")
                || RDateArry.get("resp_code").toString().equals("F")){
            logger.info("----------宝付主动还款-------回调-------订单支付结果:"
                    + RDateArry.containsKey("resp_code") + ","
                    + RDateArry.get("biz_resp_code") + ","
                    + RDateArry.get("biz_resp_msg") + ","
                    + "[trans_id:"+RDateArry.get("trans_id")+"]"
            );
            iRepaymentService.doBaoFooCallback(RDateArry);
        }else if(RDateArry.get("resp_code").toString().equals("I")){
            logger.info("----------宝付主动还款-------回调-------订单处理中！");
        }else{
            throw new Exception("反回异常！");//异常不得做为订单状态。
        }

        return "OK";//和订单状态无关，收到通知即返回OK
    }
}
