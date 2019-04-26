package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetRenewalOrder;
import com.inext.entity.ChannelInfo;
import com.inext.entity.PaymentChannel;
import com.inext.service.IAssetRenewalOrderService;
import com.inext.service.IChannelService;
import com.inext.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wenkk on 2018/3/21 09：31. 续期订单
 */
@Controller
@RequestMapping("assetRenewalOrder")
public class AssetRenewalOrderController extends BaseController {
    Logger logger = LoggerFactory.getLogger(AssetRenewalOrderController.class);

    @Resource
    IAssetRenewalOrderService iAssetRenewalOrderService;
    @Resource
    private IChannelService channelService;

    /**
     * 续期订单列表查询
     */
    @RequestMapping("pageList")
    public ModelAndView getPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        PageInfo<AssetRenewalOrder> page = null;
        if(null == params.get("init"))
            page = iAssetRenewalOrderService.getPageList(params);
        else
            page = getPage();
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        if(null == params.get("startDate") || null == params.get("endDate") || "".equals(params.get("startDate")) || "".equals(params.get("endDate")))
        {
            Map<Integer,String> dateMap=getInitQueryStartDate(true);
            params.put("startDate",dateMap.get(1));
            params.put("endDate",dateMap.get(2));
        }
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("channelId", params.get("channelId"));
        mav.addObject("paymentChannel", params.get("paymentChannel"));
        mav.addObject("orderNo",params.get("orderNo"));

        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        mav.addObject("channelList", channelList);
        mav.addObject("paymentChannels", PaymentChannel.DESCRIPTION);

        mav.setViewName("v2/assetRenewalOrder/assetRenewalOrderList");
        return mav;
    }

    /**
     * 续期订单导出
     */
    @RequestMapping("pageListExport")
    public void pageListExport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iAssetRenewalOrderService.getList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("order_id", "订单号");
            title.put("user_name", "姓名");
            title.put("user_phone", "手机号");
            title.put("money_amount", "借款金额");
            title.put("money_day", "借款期限");
            title.put("penalty_amount", "违约金");
            title.put("credit_repayment_time", "放款时间");
            title.put("renewal_time", "续期时间");
            title.put("renewal_day", "续期天数");
            title.put("repayment_time", "续期后应还款时间");
            title.put("channel_name", "渠道");
            title.put("orderno","还款单号");
            title.put("paymentTime","还款时间");
            title.put("repaymentChannel","还款渠道");
            ExcelUtil.writableWorkbook(out, list, title);
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
