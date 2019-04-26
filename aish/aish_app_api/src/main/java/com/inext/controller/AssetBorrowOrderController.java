package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.*;
import com.inext.entity.fuyou.MobilePayRecv;
import com.inext.enumerate.ApiStatus;
import com.inext.enumerate.Status;
import com.inext.result.ApiResult;
import com.inext.result.ApiServiceResult;
import com.inext.result.JsonResult;
import com.inext.result.ResponseBase;
import com.inext.service.*;
import com.inext.utils.DateUtil;
import com.inext.utils.JsonUtils;
import com.inext.utils.PhoneFormatCheckUtils;
import com.inext.view.params.PagingParams;
import com.inext.view.result.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/order")
public class AssetBorrowOrderController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(AssetBorrowOrderController.class);
    @Resource
    private IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    private IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Resource
    private IAssetLogisticsOrderService iAssetLogisticsOrderService;
    @Resource
    private IAssetRepaymentOrderService iAssetRepaymentOrderService;
    @Resource
    private IAssetRenewalOrderService iAssetRenewalOrderService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @ApiOperation(value = "订单记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "orderEnd", value = "状态.0：回收中;1：已完成", dataType = "int", paramType = "query")
    })
    @PostMapping("/orderList")
    public ApiResult<PagingResult<AssetBorrowOrderResult>> orderList(HttpServletRequest request, PagingParams pagingParams) {
        Map<String, String> params = getParameters(request);
        String token = request.getHeader("token");
        BorrowUser bu = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));

        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", bu.getId());
        param.put("orderEnd", params.get("orderEnd"));

        ApiServiceResult apiServiceResult = this.iAssetBorrowOrderService.getOrderList(pagingParams, param);

        return new ApiResult<>(apiServiceResult);
    }

    @ApiOperation(value = "订单详情")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "int", paramType = "query")
    })
    @PostMapping("/detail")
    public ApiResult<AssetBorrowOrderDetailsResult> detail(Integer id) {

        return new ApiResult<>(this.iAssetBorrowOrderService.getById(id));
    }

    @ApiOperation(value = "保存物流信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "ogisticsNo", value = "快递单号", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "ogisticsKey", value = "快递公司", dataType = "int", paramType = "query")
    })
    @PostMapping("/addLogistics")
    public ApiResult addLogistics(HttpServletRequest request, Integer orderId) {
        Map<String, String> params = getParameters(request);

        Date date = new Date();
        AssetLogisticsOrder assetLogisticsOrder = new AssetLogisticsOrder();
        assetLogisticsOrder.setOrderId(orderId);
        assetLogisticsOrder.setOgisticsNo(params.get("ogisticsNo"));
        String logisticsCompany = Constants.LOGISTICS_COMPANY.get(params.get("ogisticsKey")).toString();
        assetLogisticsOrder.setOgisticsName(logisticsCompany);
        assetLogisticsOrder.setCreateTime(date);
        int logisticsCount = iAssetLogisticsOrderService.saveAssetLogisticsOrder(assetLogisticsOrder);

        AssetRepaymentOrder assetRepaymentOrder = new AssetRepaymentOrder();
        assetRepaymentOrder.setOrderId(Integer.parseInt(params.get("orderId")));
        assetRepaymentOrder.setOrderMail(10);
        int repayMentCount = iAssetRepaymentOrderService.updateRepaymentByOrderId(assetRepaymentOrder);

        AssetBorrowOrder assetBorrowOrder = this.iAssetBorrowOrderService.getOrderById(orderId);
        if (!AssetOrderStatusHis.STATUS_FKCG.equals(assetBorrowOrder.getStatus())) {
            return new ApiResult(ApiStatus.FAIL.getCode(), "当前订单状态不允许继续添加物流信息");
        }
        AssetBorrowOrder updateOrder = new AssetBorrowOrder();
        updateOrder.setId(orderId);
        updateOrder.setStatus(AssetOrderStatusHis.STATUS_YJC);
        iAssetBorrowOrderService.updateByPrimaryKeySelective(updateOrder);

        AssetOrderStatusHis his = new AssetOrderStatusHis();
        his.setOrderId(orderId);
        his.setOrderStatus(AssetOrderStatusHis.STATUS_YJC);
        his.setRemark("物流信息已提交，客服会在收到商品后进行质检。\\n物流公司:" + logisticsCompany + "\\n物理单号:" + params.get("ogisticsNo"));
        his.setCreateTime(date);
        iAssetOrderStatusHisService.saveHis(his);

        return new ApiResult<>();

    }

    @ApiOperation(value = "获取订单创建前信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "equipmentId", value = "设备id", dataType = "int", paramType = "query", required=false)
    })
    @PostMapping("/getOrderBefore")
    public ApiResult getOrderBefore(HttpServletRequest request) {
        Map<String, String> pams = this.getParameters(request);
        BorrowUser bu = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));
        String equipmentId = pams.get("equipmentId");
        ApiServiceResult apiServiceResult =null;
        if(equipmentId ==null || equipmentId.equals(""))
        {
            apiServiceResult = iAssetBorrowOrderService.getOrderBeforeNoEquipmentId(bu);
        }else {
            apiServiceResult = iAssetBorrowOrderService.getOrderBefore(equipmentId, bu);
        }
        return new ApiResult(apiServiceResult);
    }

    @ApiOperation(value = "获取续期创建前信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query")
    })
    @PostMapping("/renewalBeforeInfo")
    public ApiResult<RenewalBeforeResult> renewalBeforeInfo(HttpServletRequest request) {
        Map<String, String> pams = this.getParameters(request);
        if(StringUtils.isNotEmpty(pams.get("orderId"))) {
            int orderId=Integer.parseInt(pams.get("orderId"));
            AssetBorrowOrder order = this.iAssetBorrowOrderService.getOrderById(orderId);
            if (order != null && order.getId() > 0) {
                //查询续期限制次数
                // 查询需要拒绝的渠道
                Map<String, String> mapRefuse = backConfigParamsService.getBackConfig(BackConfigParams.SYS_FEE, null);
                //查询续期记录
                Map<String,Object> params = new HashMap<>();
                params.put("orderId", order.getId());
                int count = iAssetRenewalOrderService.getCount(params);
                if(order.getStatus() == AssetOrderStatusHis.STATUS_YYQ){
                    //已逾期不能续期
                    return new ApiResult(new ApiServiceResult(ApiStatus.FAIL.getCode(), "该订单已逾期, 无法续期"));
                }else if(count >= NumberUtils.toInt(mapRefuse.get("renewal_number"))){
                    //续期次数超过限制不能续期
                    return new ApiResult(new ApiServiceResult(ApiStatus.FAIL.getCode(), "该订单续期次数超过限制, 无法续期"));
                }else{
                    //可以续期
                    int loan_day = order.getMoneyDay();
                    int loan_wy = order.getPenaltyAmount().intValue();
                    return new ApiResult(new RenewalBeforeResult(loan_day, loan_wy));
                }
            }
        }
        return new ApiResult(new ApiServiceResult(ApiStatus.FAIL.getCode(), "订单号 不存在"));
    }

//    @ApiOperation(value = "取消订单前数据信息")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
//            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query")
//    })
//    @PostMapping("/cancelOrderBeforeInfo")
//    public ApiResult<MapResult> cancelOrderBeforeInfo(HttpServletRequest request) {
//
//        Map<String, String> params = getParameters(request);
//        String orderIdStr = params.get("orderId");
//        Integer orderId = 0;
//        if (StringUtils.isEmpty(orderIdStr)) {
//            return new ApiResult(new ApiServiceResult(ApiStatus.FAIL.getCode(), "订单号 不存在"));
//        } else {
//            orderId = Integer.parseInt(orderIdStr);
//        }
//        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
//        AssetRepaymentOrder assetRepaymentOrder= iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);
//        Map map = new HashMap();
//        if(assetRepaymentOrder.getRepaymentedAmount().compareTo(order.getPerPayMoney())!=1){
//            map.put("perPayMoney", order.getPerPayMoney().subtract(assetRepaymentOrder.getRepaymentedAmount()));
//            map.put("penaltyAmount", order.getPenaltyAmount());
//            map.put("lateMoney", order.getLateMoney());
//        }else {
//            if(assetRepaymentOrder.getRepaymentedAmount().compareTo(order.getPenaltyAmount().add(order.getPerPayMoney()))!=1){
//                map.put("perPayMoney", 0);
//                map.put("penaltyAmount", order.getPenaltyAmount().add(order.getPerPayMoney()).subtract(assetRepaymentOrder.getRepaymentedAmount()));
//                map.put("lateMoney", order.getLateMoney());
//            }else{
//                map.put("perPayMoney", 0);
//                map.put("penaltyAmount", 0);
//                map.put("lateMoney", order.getLateMoney().add(order.getPenaltyAmount()).add(order.getPerPayMoney()).subtract(assetRepaymentOrder.getRepaymentedAmount()));
//            }
//        }
//        map.put("lateDay", order.getLateDay());
//
//        return new ApiResult(new MapResult(map));
//    }

    @ApiOperation(value = "取消订单前数据信息")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"), @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query")})
    @PostMapping("/cancelOrderBeforeInfo")
    public ApiResult<MapResult> cancelOrderBeforeInfo(HttpServletRequest request)
    {

        Map<String, String> params = getParameters(request);
        String orderIdStr = params.get("orderId");
        Integer orderId = 0;
        if (StringUtils.isEmpty(orderIdStr))
        {
            return new ApiResult(new ApiServiceResult(ApiStatus.FAIL.getCode(), "订单号 不存在"));
        }
        else
        {
            orderId = Integer.parseInt(orderIdStr);
        }
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(orderId);
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

        BigDecimal ceditAmount = assetRepaymentOrder.getCeditAmount();

        BigDecimal perPayMoney = new BigDecimal(0);
        BigDecimal penaltyAmount = new BigDecimal(0);
        BigDecimal lateMoney = new BigDecimal(0);
        // 100 - 80 - 20
        if (assetRepaymentOrder.getRepaymentedAmount().compareTo(order.getPerPayMoney()) != 1)
        {
            perPayMoney = order.getPerPayMoney().subtract(assetRepaymentOrder.getRepaymentedAmount());
            penaltyAmount = order.getPenaltyAmount();
            lateMoney = order.getLateMoney();
        }
        else
        {
            if (assetRepaymentOrder.getRepaymentedAmount().compareTo(order.getPenaltyAmount().add(order.getPerPayMoney())) != 1)
            {
                perPayMoney = new BigDecimal(0);
                penaltyAmount = order.getPenaltyAmount().add(order.getPerPayMoney()).subtract(assetRepaymentOrder.getRepaymentedAmount());
                lateMoney = order.getLateMoney();
            }
            else
            {
                perPayMoney = new BigDecimal(0);
                penaltyAmount = new BigDecimal(0);
                lateMoney = order.getLateMoney().add(order.getPenaltyAmount()).add(order.getPerPayMoney()).subtract(assetRepaymentOrder.getRepaymentedAmount());
            }
        }

        //新加的减免金额业务
        if (ceditAmount.compareTo(new BigDecimal(0)) > 0)
        {
            if (perPayMoney.compareTo(ceditAmount) >= 0)
            {
                perPayMoney = perPayMoney.subtract(ceditAmount);
            }
            else
            {
                ceditAmount = ceditAmount.subtract(perPayMoney);
                if (penaltyAmount.compareTo(ceditAmount) >= 0)
                {
                    perPayMoney = new BigDecimal(0);
                    penaltyAmount = penaltyAmount.subtract(ceditAmount);
                }
                else
                {
                    ceditAmount = ceditAmount.subtract(penaltyAmount);
                    perPayMoney = new BigDecimal(0);
                    penaltyAmount = new BigDecimal(0);
                    lateMoney = lateMoney.subtract(ceditAmount);
                }
            }
        }

        map.put("perPayMoney", perPayMoney);
        map.put("penaltyAmount", penaltyAmount);
        map.put("lateMoney", lateMoney);
        map.put("lateDay", new BigDecimal(order.getLateDay()));

        return new ApiResult(new MapResult(map));
    }

    @ApiOperation(value = "物流前数据信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query")
    })
    @PostMapping("/logisticsBeforeInfo")
    public ApiResult<LogisticsInfoResult> logisticsBeforeInfo(HttpServletRequest request) {
        Integer orderId = 0;
        Map<String, String> params = getParameters(request);
        String orderIdStr = params.get("orderId");
        if (StringUtils.isEmpty(orderIdStr)) {
            return new ApiResult(new ApiServiceResult(ApiStatus.FAIL.getCode(), "订单号 不存在"));
        } else {
            orderId = Integer.parseInt(orderIdStr);
        }
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(orderId);
        LogisticsInfoResult result = new LogisticsInfoResult();
        result.setResInfo(order.getDeviceModel() + "*1,USB连接线*1,USB电源适配器*1");
        result.setLogisticsCompany(Constants.LOGISTICS_COMPANY);
        return new ApiResult(result);
    }

    @ApiOperation(value = "查询订单状态")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "phone", value = "用户手机号码", dataType = "String", paramType = "query")})
    @PostMapping(value="/queryStatus")
    public ResponseBase queyOrderStatus(HttpServletRequest request,String phone) {
        ResponseBase result=ResponseBase.getInitResponse();
        logger.info("++++++++++++++++++++++++++++++++++++++++++++");
        List<AssetBorrowOrder> orders = iAssetBorrowOrderService.queyOrderStatus(phone);
        if (orders.size()>0) {
            result.setResponse(true);
            result.setResponseCode(200);

            result.setResponseMsg("有拒绝的订单");
        } else {
            result.setResponse(false);
            result.setResponseCode(200);
            result.setResponseMsg("没有拒绝的订单!");
        }
        return result;
    }
}
