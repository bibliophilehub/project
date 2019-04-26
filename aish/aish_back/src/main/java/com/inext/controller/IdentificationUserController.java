package com.inext.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.inext.entity.ChannelInfo;
import com.inext.service.IChannelService;
import com.inext.service.IIdentificationUserService;


@Controller
@RequestMapping(value = "/data")
public class IdentificationUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(IdentificationUserController.class);
    @Resource
    private IIdentificationUserService identificationUserService;
    @Resource
    private IChannelService channelService;

    //查询认证
    @RequestMapping("/identificationUserList")
    public String orderList(HttpServletRequest request, Model model) {
        try {
        	String curChannelName="全部";
            Map<String, Object> params = this.getParametersO(request);
            if (null!=params.get("registerTime")) {
            	model.addAttribute("registerTime", params.get("registerTime"));
            }
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
            model.addAttribute("curChannelName", curChannelName);
            
            
            PageInfo<Map<String, Object>> list = identificationUserService.getPageList(params);
            logger.info(JSONObject.toJSONString(list));
            model.addAttribute("pageInfo", list);
            
            List<ChannelInfo> channelList=channelService.getList(new HashMap<String, Object>());
            model.addAttribute("channelList", channelList);
            
            
            
            
            
        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/data/identificationUserList";
    }


}
