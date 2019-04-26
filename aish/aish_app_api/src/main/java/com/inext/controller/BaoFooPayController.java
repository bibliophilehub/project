package com.inext.controller;

import com.inext.constants.RedisCacheConstants;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.fuyou.Fuiou;
import com.inext.result.ResponseDto;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IRepaymentService;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/baoFooPay")
public class BaoFooPayController extends BaseController {

    private final static String BAOFOOPAY_KEY="BAOFOOPAY_KEY:";

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RelyController.class);
    @Resource
    IRepaymentService iRepaymentService;

    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;

    @Resource(name = "redisUtil")
    RedisUtil redisUtil;

    @ApiOperation(value = "用户还款接口  H5页面", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "还款类型(1取消订单，2续期)", dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/userPay")
    public ModelAndView userPay(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
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
        if(redisUtil.exists(BAOFOOPAY_KEY+orderId)&&redisUtil.get(BAOFOOPAY_KEY+orderId).toString().equalsIgnoreCase(typeStr)){//如果存在就 说明发起过 直接拒绝
            mav.addObject("msg", "您已发起此操作，请耐心等待！");
            mav.setViewName("reUrl");
            return mav;
        }else if (type==1){
            AssetBorrowOrder order=iAssetBorrowOrderService.getOrderById(orderId);
            if (order.getStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
                mav.addObject("msg", "您的订单已取消，无需重复取消");
                mav.setViewName("reUrl");
            }
        }

        redisUtil.set(BAOFOOPAY_KEY+orderId,typeStr,60*3L);
        Fuiou fuiou=iRepaymentService.getRepaymentFromParams(userId,orderId,type);
        mav.addObject("fuyouApiParams", fuiou.getFuyouApiParams());
        mav.addObject("FM", fuiou.getFM());
        mav.addObject("formUrl", Fuiou.URL);
        mav.setViewName("baoFooPay");
        return mav;
    }

    @PostMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getParameters(request);
        logger.info("callback",params);
       String responseCode =params.get("RESPONSECODE");//响应代码
        redisUtil.remove(BAOFOOPAY_KEY+params.get("MCHNTORDERID"));
        if(StringUtils.isNotEmpty(responseCode)){
            int isOk=iRepaymentService.doCcallback(params);
            if (isOk==0){
                response.setStatus(202);
            }
        }else {
            response.setStatus(202);
        }
    }

    @RequestMapping(value = "homeurl")
    public String homeurl(HttpServletRequest request, HttpServletResponse response) {
        return "homeUrl";
    }
    @RequestMapping(value = "reurl")
    public String reurl(HttpServletRequest request, HttpServletResponse response) {
        return "reUrl";
    }
}
