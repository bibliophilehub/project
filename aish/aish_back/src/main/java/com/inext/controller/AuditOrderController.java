package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.BackUser;
import com.inext.enumerate.Status;
import com.inext.result.AjaxResult;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.utils.DateUtil;
import com.inext.utils.ExcelUtil;
import com.inext.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/audit")
public class AuditOrderController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(AuditOrderController.class);

    @Resource
    private IAssetBorrowOrderService iAssetBorrowOrderService;

    @RequestMapping("/orderList")
    public String orderList(HttpServletRequest request, Model model, AssetBorrowOrder order) {
        try {
            String hasParamStatus = "0";
            Map<String, Object> params = this.getParametersO(request);
            params.put("status", AssetOrderStatusHis.STATUS_DRGSH);

            model.addAttribute("hasParamStatus", hasParamStatus);
            model.addAttribute("startDate", params.get("startDate"));
            model.addAttribute("endDate", params.get("endDate"));
            model.addAttribute("order", order);
            model.addAttribute("orderStatus", AssetOrderStatusHis.orderStatusMap);

            PageInfo<AssetBorrowOrder> list = iAssetBorrowOrderService.getOrderPageList(params);
            model.addAttribute("pageInfo", list);
        } catch (Exception e) {
            logger.error("获取待人工审核订单异常：", e);
        }
        return "v2/order/auditOrderList";
    }

    @RequestMapping("orderExport")
    public void orderExport(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());

            Map<String, Object> params = super.getParametersO(request);
            params.put("status", AssetOrderStatusHis.STATUS_DRGSH);
            List<AssetBorrowOrder> orderList = iAssetBorrowOrderService.getOrderList(params);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (orderList != null && orderList.size() > 0) {
                for (AssetBorrowOrder order : orderList) {
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("id", order.getId());
                    temp.put("channelName", order.getChannelName());
                    temp.put("userName", order.getUserName());
                    temp.put("userPhone", order.getUserPhone());
                    temp.put("deviceModel", order.getDeviceModel());
                    temp.put("perPayMoney", order.getPerPayMoney());
                    temp.put("moneyDay", order.getMoneyDay());
                    temp.put("penaltyAmount", String.valueOf(order.getPenaltyAmount()));
                    temp.put("orderTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getOrderTime()));
                    temp.put("loanTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getLoanTime()));
                    String status = AssetOrderStatusHis.orderStatusMap.get(order.getStatus());
                    temp.put("status", status);
                    temp.put("score", order.getScore());
                    temp.put("detail", order.getDetail());
                    temp.put("updateTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getUpdateTime()));
                    list.add(temp);
                }
            }

            if (list.size() > 0) {
                Map<String, String> title = new LinkedHashMap<>();
                title.put("id", "订单号");
                title.put("channelName", "渠道");
                title.put("userName", "姓名");
                title.put("userPhone", "手机号");
                title.put("deviceModel", "手机型号");
                title.put("perPayMoney", "借款金额");
                title.put("moneyDay", "借款期限");
                title.put("penaltyAmount", "违约金");
                title.put("orderTime", "申请时间");
                title.put("loanTime", "放款时间");
                title.put("status", "状态");
                title.put("score", "风控分");
                title.put("detail", "风控消息");
                title.put("updateTime", "更新时间");
                ExcelUtil.writeWorkbook(out, list, title);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getExlName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date()) + System.currentTimeMillis() + ".xls";
    }

    @RequestMapping("resultNotify")
    @ResponseBody
    public AjaxResult resultNotify(HttpServletResponse response, HttpServletRequest request, Integer orderId, Integer status, String remark) {
        BackUser operator = (BackUser) RequestUtils.getRequest().getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        if (orderId == null || status == null) {
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        }
        iAssetBorrowOrderService.auditOrder(orderId, status, remark, operator.getAccount());
        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }
}
