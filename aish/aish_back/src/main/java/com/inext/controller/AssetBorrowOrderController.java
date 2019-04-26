package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.ChannelInfo;
import com.inext.entity.PaymentChannel;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IChannelService;
import com.inext.utils.DateUtil;
import com.inext.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "/assetBorrowOrder")
public class AssetBorrowOrderController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(AssetBorrowOrderController.class);
    @Resource
    private IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    private IChannelService channelService;

    @RequestMapping("/orderList")
    public String orderList(HttpServletRequest request, Model model, AssetBorrowOrder order) {
        try {
            String hasParamStatus = "0";
            Map<String, Object> params = this.getParametersO(request);
            if (null != params.get("hasParamStatus")) {
                hasParamStatus = params.get("hasParamStatus").toString();
            } else {
                model.addAttribute("status", params.get("status").toString());
            }
            if (null != params.get("channelId")) {
                model.addAttribute("channelId", params.get("channelId"));
            }
            model.addAttribute("hasParamStatus", hasParamStatus);
            if("3".equals(params.get("status")) && "1".equals(params.get("loaSucess")))
            {
                if (null == params.get("startLoanDate") || null == params.get("endLoanDate") || "".equals(params.get("startLoanDate")) || "".equals(params.get("endLoanDate"))) {
                    Map<Integer, String> dateMap = getInitQueryStartDate(true);
                    params.put("startLoanDate", dateMap.get(1));
                    params.put("endLoanDate", dateMap.get(2));
                }
            }
            else {
                if (null == params.get("startDate") || null == params.get("endDate") || "".equals(params.get("startDate")) || "".equals(params.get("endDate"))) {
                    Map<Integer, String> dateMap = getInitQueryStartDate(true);
                    params.put("startDate", dateMap.get(1));
                    params.put("endDate", dateMap.get(2));
                }
            }
            model.addAttribute("startDate", params.get("startDate"));
            model.addAttribute("endDate", params.get("endDate"));
            model.addAttribute("startLoanDate", params.get("startLoanDate"));
            model.addAttribute("endLoanDate", params.get("endLoanDate"));
            model.addAttribute("order", order);
            model.addAttribute("orderStatus", AssetOrderStatusHis.orderStatusMap);
            model.addAttribute("paymentChannel", params.get("paymentChannel"));
            model.addAttribute("noOrder",params.get("noOrder"));

            if (null != params.get("status") && "3".equals(params.get("status").toString())) {
                params.remove("status");
                params.remove("status");
                params.put("loanSuccess", "loanSuccess");
            }
            PageInfo<AssetBorrowOrder> list =null;
            if(null != params.get("init")) {
                list  = getPage();
            }
            else
                list = iAssetBorrowOrderService.getOrderPageList(params);
            model.addAttribute("pageInfo", list);
            List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
            model.addAttribute("channelList", channelList);
            model.addAttribute("paymentChannels", PaymentChannel.DESCRIPTION);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/order/orderList";
    }

    @RequestMapping("orderExport")
    public void orderExport(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            Map<String, Object> param = super.getParametersO(request);

            if (null != param.get("status") && "3".equals(param.get("status").toString())) {
                param.remove("status");
                param.put("loanSuccess", "loanSuccess");
            }
            List<AssetBorrowOrder> orderList = iAssetBorrowOrderService.getOrderList(param);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (orderList != null && orderList.size() > 0) {
                for (AssetBorrowOrder order : orderList) {
                    Map<String, Object> temp = new HashMap<String, Object>();

                    temp.put("id", order.getId());
                    temp.put("userName", order.getUserName());
                    temp.put("userPhone", order.getUserPhone());
                    temp.put("deviceModel", order.getDeviceModel());
                    temp.put("deviceNumber", order.getDeviceNumber());
                    temp.put("moneyAmount", order.getPerPayMoney());
                    temp.put("moneyDay", order.getMoneyDay());
                    temp.put("penaltyAmount", String.valueOf(order.getPenaltyAmount()));
                    temp.put("channelName", order.getChannelName());
                    temp.put("orderTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getOrderTime()));
                    temp.put("loanTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getLoanTime()));
                    String status = AssetOrderStatusHis.orderStatusMap.get(order.getStatus());

                    temp.put("status", status);
                    temp.put("score", order.getScore());
                    temp.put("detail", order.getDetail());
                    temp.put("updateTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", order.getUpdateTime()));
                    temp.put("apply_new", order.getApplyNew() == 0 ? "否" : "是");
                    temp.put("noOrder",order.getNoOrder());

                    list.add(temp);
                }
            }

            if (list != null && list.size() > 0) {
                Map<String, String> title = new LinkedHashMap<>();
                title.put("id", "订单号");
                title.put("userName", "姓名");
                title.put("userPhone", "手机号");
                title.put("deviceModel", "手机型号");
                title.put("deviceNumber", "设备号");
                title.put("moneyAmount", "借款金额");
                title.put("moneyDay", "借款期限");
                title.put("penaltyAmount", "违约金");
                title.put("status", "状态");
                title.put("score", "风控分");
                title.put("detail", "风控消息");
                title.put("orderTime", "申请时间");
                title.put("noOrder","代付单号");
                title.put("loanTime", "放款时间");
                title.put("updateTime", "更新时间");
                title.put("channelName", "渠道");
                title.put("apply_new", "老用户");
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
}
