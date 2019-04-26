package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.ChannelInfo;
import com.inext.entity.PaymentChannel;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.service.IChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 催收
 */
@Controller
@RequestMapping(value = "/collectionManagement")
public class CollectionController extends BaseController{
    private static Logger logger = LoggerFactory.getLogger(AssetBorrowOrderController.class);
    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;
    @Resource
    private IChannelService channelService;

    /**
     * 已逾期
     */
    @RequestMapping("pageList")
    public ModelAndView getPageList(HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        params.put("type","7");
        PageInfo<AssetRepaymentOrder> page = null;
        if(null == params.get("init"))
            page = iAssetRepaymentOrderService.getPageList(params);
        else
            page = getPage();
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("orderStatus", params.get("orderStatus"));
        if(null == params.get("startDate") || null == params.get("endDate") || "".equals(params.get("startDate")) || "".equals(params.get("endDate")))
        {
            Map<Integer,String> dateMap = getInitQueryStartDate(true);
            params.put("startDate",dateMap.get(1));
            params.put("endDate",dateMap.get(2));
        }
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("startRepayDate", params.get("startRepayDate"));
        mav.addObject("endRepayDate", params.get("endRepayDate"));
        mav.addObject("channelId", params.get("channelId"));
        mav.addObject("repaymentType", params.get("repaymentType"));
        mav.addObject("paymentChannel", params.get("paymentChannel"));
        mav.addObject("sortColumn",params.get("sortColumn"));//排序
        mav.addObject("lateLevel",params.get("lateLevel"));//逾期手别查询

        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        mav.addObject("channelList", channelList);
        mav.addObject("paymentChannels", PaymentChannel.DESCRIPTION);

        mav.setViewName("v2/collection/lateOrderList");

        return mav;
    }
    /**
     * 已还款列表查询
     */
    @RequestMapping("repaymentedPgeList")
    public ModelAndView getRepaymentedPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        params.put("type","8");
        PageInfo<AssetRepaymentOrder> page = null;
        if(null == params.get("init")) {
            page = iAssetRepaymentOrderService.getListForRepaymented(params);
        }
        else
            page = getPage();
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("orderStatus", params.get("orderStatus"));
        //已还款列表查询以实际放款时间算
        if (null == params.get("startRepayDate") || null == params.get("endRepayDate") || "".equals(params.get("startRepayDate")) || "".equals(params.get("endRepayDate"))) {
            Map<Integer, String> dateMap = null;
            dateMap = getInitQueryStartDate(true);
            params.put("startRepayDate", dateMap.get(1));
            params.put("endRepayDate", dateMap.get(2));
        }
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("startRepayDate", params.get("startRepayDate"));
        mav.addObject("endRepayDate", params.get("endRepayDate"));
        mav.addObject("channelId", params.get("channelId"));
        mav.addObject("repaymentType", params.get("repaymentType"));
        mav.addObject("paymentChannel", params.get("paymentChannel"));
        mav.addObject("orderno",params.get("orderno"));//还款单号查询,已还款页面
        mav.addObject("sortColumn",params.get("sortColumn"));//排序
        mav.addObject("lateLevel",params.get("lateLevel"));//逾期手别查询

        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        mav.addObject("channelList", channelList);
        mav.addObject("paymentChannels", PaymentChannel.DESCRIPTION);
        mav.addObject("repaymentTypeMap", AssetRepaymentOrder.repaymentTypeMap);
        mav.setViewName("v2/collection/assetRepaymentedOrderList");
        return mav;
    }
}
