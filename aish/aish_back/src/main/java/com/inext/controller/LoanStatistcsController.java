package com.inext.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inext.entity.ChannelInfo;
import com.inext.service.IChannelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;
import com.inext.service.ILoanStatisticsService;
import com.inext.utils.ExcelUtil;


@Controller
@RequestMapping(value = "/data")
public class LoanStatistcsController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(LoanStatistcsController.class);
    @Resource
    private ILoanStatisticsService loanStatisticsService;
    @Resource
    private IChannelService channelService;

    @RequestMapping("/loanStatisticsList")
    public String orderList(HttpServletRequest request, Model model) {
        try {
            Map<String, Object> params = this.getParametersO(request);
            
            if (null != params.get("startDate")) {
                model.addAttribute("startDate", params.get("startDate").toString());
            }
            if (null != params.get("endDate")) {
            	model.addAttribute("endDate", params.get("endDate").toString());
            }

            String curChannelName="全部";
            if (null!=params.get("channelId")) {
                model.addAttribute("channelId", params.get("channelId"));
                if("-1".equals(params.get("channelId"))){
                    params.remove("channelId");
                }else{
                    ChannelInfo channel=channelService.getChannelById(Integer.parseInt(params.get("channelId").toString()));
                    if(null!=channel){
                        curChannelName=channel.getChannelName();
                    }
                }
            }
            List<ChannelInfo> channelList=channelService.getList(new HashMap<String, Object>());
            model.addAttribute("channelList", channelList);

            model.addAttribute("curChannelName", curChannelName);
            PageInfo<Map<String,Object>> list = loanStatisticsService.getPageList(params);
            model.addAttribute("pageInfo", list);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/data/loanStatistics";
    }

    /**
     * 导出
     */
    @RequestMapping("/exportLoanExcelFile")
    public void exportLoanExcelFile(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        String curChannelName="全部";
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("放款统计信息.xls".getBytes("GB2312"), "iso8859-1"));
            Map<String, Object> params = this.getParametersO(request);
            List<Map<String, Object>> list = loanStatisticsService.exportExcelFile(params);

            if (null!=params.get("channelId") && CollectionUtils.isNotEmpty(list)) {
                if("-1".equals(params.get("channelId"))){
                    params.remove("channelId");
                }else if("0".equals(params.get("channelId"))){
                    curChannelName="自然流量";
                }else{
                    ChannelInfo channel=channelService.getChannelById(Integer.parseInt(params.get("channelId").toString()));
                    if(null!=channel){
                        curChannelName=channel.getChannelName();
                    }
                }
            }
            
            if (list != null && list.size() != 0) {
                LinkedHashMap<String, String> title = new LinkedHashMap<>();
                title.put("applyTime", "申请日期");
                title.put("newApplyQuantity", "新用户申请人数");
                title.put("newAuditQuantity", "新用户审核通过人数");
                title.put("newRate", "新用户审核通过率");
                title.put("oldApplyQuantity", "老用户申请人数");
                title.put("oldAuditQuantity", "老用户审核通过人数");
                title.put("oldRate", "老用户审核通过率");
                title.put("loanMoneyCount", "应放款总额");
                title.put("loanMoneyCountSuccess", "放款成功总额");
                title.put("loanFail", "放款失败单数");
                title.put("rate", "审核通过率");
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
}
