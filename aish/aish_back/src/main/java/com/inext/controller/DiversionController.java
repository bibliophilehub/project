package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.entity.BackUser;
import com.inext.entity.ChannelStatistics;
import com.inext.service.IChannelService;
import com.inext.service.IChannelStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/diversion")
public class DiversionController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DiversionController.class);

    @Autowired
    private IChannelStatisticsService channelStatisticsService;
    @Autowired
    private IChannelService channelService;

    //当天统计记录
    @RequestMapping(value = "/statistics")
    public String statistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            BackUser user = loginAdminUser(request);
            Map<String, Object> statistics = null;
            if(user.getDiversionChannel() != null && !"".equals(user.getDiversionChannel())) {
                int channelId = Integer.valueOf(user.getDiversionChannel());
                statistics = channelService.getStatisticsByChannelId(channelId);
            }
            model.addAttribute("statistics", statistics);
        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/diversion/diversionStatistics";
    }

    //历史统计记录
    @RequestMapping(value = "/statisticsHis")
    public String statisticsHis(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Map<String, Object> params = this.getParametersO(request);
            BackUser user = loginAdminUser(request);
            PageInfo<Map<String, Object>> list = new PageInfo<Map<String, Object>>();
            if(user.getDiversionChannel() != null && !"".equals(user.getDiversionChannel())) {
                int channelId = Integer.valueOf(user.getDiversionChannel());
                list = channelStatisticsService.findDiversionStatisticsHisPage(channelId, params);
            }
            model.addAttribute("pageInfo", list);
        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/diversion/diversionStatisticsHis";
    }
}
