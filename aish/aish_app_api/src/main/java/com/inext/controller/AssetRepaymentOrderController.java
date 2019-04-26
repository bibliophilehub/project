package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.fuyou.MobilePayRecv;
import com.inext.enumerate.Status;
import com.inext.result.ResponseDto;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IAssetOrderStatusHisService;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.utils.JsonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping(value = "/repayment")
public class AssetRepaymentOrderController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(AssetRepaymentOrderController.class);
    @Resource
    private IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    private IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Resource
    private IAssetRepaymentOrderService iAssetRepaymentOrderService;

    @ApiOperation(value = "还款回调", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "int", paramType = "query")
    })
    @RequestMapping("/repayment-back")
    public void renewalBack(MobilePayRecv payRecv, HttpServletRequest request, HttpServletResponse response) {
        String code = Status.FAIL.getName();
        String msg = "";
        Map<String, String> params = getParameters(request);
        String orderId = params.get("orderId");

        JSONObject json = new JSONObject();
        try {
            if (payRecv.toSign().equals(payRecv.getSIGN())) {


                if ("0000".equals(payRecv.getRESPONSECODE())) {

                    Date now = new Date();
                    //1借款单关闭订单
                    AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
                    assetBorrowOrder.setId(Integer.parseInt(orderId));
                    assetBorrowOrder.setOrderEnd(1);
                    assetBorrowOrder.setUpdateTime(now);
                    iAssetBorrowOrderService.updateByPrimaryKeySelective(assetBorrowOrder);
                    //2还款单完成订单
                    AssetRepaymentOrder repayment = iAssetRepaymentOrderService.getRepaymentByOrderId(Integer.parseInt(orderId));

                    AssetRepaymentOrder repay = new AssetRepaymentOrder();
                    repay.setId(repayment.getId());
                    repay.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
                    repay.setRepaymentRealTime(now);
                    iAssetRepaymentOrderService.update(repay);

                    //记录状态变更
                    AssetOrderStatusHis his = new AssetOrderStatusHis();
                    his.setOrderId(Integer.parseInt(params.get("orderId")));
                    his.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
                    his.setRemark("订单已完成");
                    his.setCreateTime(now);

                    iAssetOrderStatusHisService.saveHis(his);

                    code = "0";
                } else {
                    msg = "续期失败";
                }


            } else {
                msg = "参数错误";
            }

        } catch (Exception e) {
            logger.error("  error ", e);
        }

        json.put("code", code);
        json.put("message", msg);
        JsonUtils.toJson(response, json);
    }
}
