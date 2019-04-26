package com.inext.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.inext.entity.ChannelInfo;
import com.inext.service.IChannelService;
import com.inext.service.IRepaymentStatisticsService;
import com.inext.utils.ExcelUtil;


@Controller
@RequestMapping(value = "/data")
public class RepaymentStatistcsController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RepaymentStatistcsController.class);
    @Resource
    private IRepaymentStatisticsService repaymentStatisticsService;

    @Resource
    private IChannelService channelService;

    @RequestMapping("/repaymentStatisticsList")
    public String orderList(HttpServletRequest request, Model model) {
	
//        try {
//        	
//            List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
//            model.addAttribute("channelList", channelList);
//        	
//        	String curChannelName = "全部";
//            Map<String, Object> params = this.getParametersO(request);
//
//            if (null != params.get("repaymentDate")) {
//                model.addAttribute("repaymentDate", params.get("repaymentDate"));
//            }
//
//            if (null != params.get("channelId")) {
//                model.addAttribute("channelId", params.get("channelId"));
//                if ("-1".equals(params.get("channelId"))) {
//                    params.remove("channelId");
//                } else if ("0".equals(params.get("channelId"))) {
//                    curChannelName = "自然流量";
//                } else {
//                    ChannelInfo channel = channelService.getChannelById(Integer.parseInt(params.get("channelId").toString()));
//                    if (null != channel) {
//                        curChannelName = channel.getChannelName();
//                    }
//                }
//            }
//            
//            model.addAttribute("curChannelName", curChannelName);
//            
//            PageInfo<Map<String, Object>> list =  repaymentStatisticsService.getRepayStatistics(params);
//            model.addAttribute("pageInfo", list);
//
//            
//          } catch (Exception e) {
//          logger.error("back  error ", e);
//      }
//
//        return "v2/data/repaymentStatistics";
        
        
        try {

            String curChannelName = "全部";
            Map<String, Object> params = this.getParametersO(request);

            if (null != params.get("repaymentDate")) {
                model.addAttribute("repaymentDate", params.get("repaymentDate"));
            }

            if (null != params.get("channelId")) {
                model.addAttribute("channelId", params.get("channelId"));
                if ("-1".equals(params.get("channelId"))) {
                    params.remove("channelId");
                } else if ("0".equals(params.get("channelId"))) {
                    curChannelName = "自然流量";
                } else {
                    ChannelInfo channel = channelService.getChannelById(Integer.parseInt(params.get("channelId").toString()));
                    if (null != channel) {
                        curChannelName = channel.getChannelName();
                    }
                }
            }
            model.addAttribute("curChannelName", curChannelName);


            PageInfo<Map<String, Object>> list = repaymentStatisticsService.getPageList(params);
            logger.info(JSONObject.toJSONString(list));
            model.addAttribute("pageInfo", list);

            List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
            model.addAttribute("channelList", channelList);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/data/repaymentStatistics";
    }

    /**
     * 导出
     */
    @RequestMapping("/exportRepaymentExcelFile")
    public void exportRepaymentExcelFile(HttpServletResponse response, HttpServletRequest request) {

        try (ServletOutputStream out = response.getOutputStream()) {

            String curChannelName = "全部";

            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("还款统计信息.xls".getBytes("GB2312"), "iso8859-1"));
            Map<String, Object> params = this.getParametersO(request);
            if (null != params.get("channelId")) {
                if ("-1".equals(params.get("channelId"))) {
                    params.remove("channelId");
                } else if ("0".equals(params.get("channelId"))) {
                    curChannelName = "自然流量";
                } else {
                    ChannelInfo channel = channelService.getChannelById(Integer.parseInt(params.get("channelId").toString()));
                    if (null != channel) {
                        curChannelName = channel.getChannelName();
                    }
                }
            }
            List<Map<String, Object>> list = repaymentStatisticsService.exportExcelFileStatistics(params);


            if (list != null && list.size() > 0) {
                // for (Map<String, Object> loan : list) {
                //     loan.put("curChannelName", curChannelName);
                // }

                final String currChannelName = curChannelName;
                list.stream().forEach((map) -> {
                    map.put("curChannelName", currChannelName);
                });


                Map<String, String> title = new LinkedHashMap<>();
                title.put("curChannelName", "序号");
                title.put("loan_date", "放款日");
                title.put("repay_date", "还款日");
                title.put("shoujie", "首借");
                title.put("fujie", "复借");
                title.put("loan_all_count", "放款总单数");
                title.put("history_xuqi", "历史续期到期数");
                title.put("xuqi", "续期单数");
                title.put("anshi_huankuan", "按时还款单数");
                title.put("yuqi_danshu", "逾期总单数");
                title.put("yuqi_yihuan", "逾期已还单数");
                title.put("daihuan_danshu", "当前待还单数");
                title.put("shouri_yuqilv", "首日逾期率");
                title.put("day7_yuqilv", "7日逾期率");
                title.put("now_huikuanlv_jine", "当前回款率(金额)");
                title.put("now_huikuanlv_xuqi", "当前回款率(含续期)");
                title.put("shoujie_huikuanlv_xuqi", "首借回款率(含续期)");
                title.put("fujie_huikuanlv_xuqi", "复借回款率(含续期)");
                
                ExcelUtil.writeWorkbook(out, list, title);
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
