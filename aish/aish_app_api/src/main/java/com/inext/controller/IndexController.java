package com.inext.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inext.entity.AssetBorrowOrder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.enumerate.Status;
import com.inext.result.ServiceResult;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.utils.JSONUtil;
import com.inext.utils.JsonUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 系统设置
 * @author cannavaro
 *
 */
@RestController
@RequestMapping(value = "/app_sys")
public class IndexController extends BaseController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IBackConfigParamsService backConfigParamsService;
    @Autowired
    private IAssetBorrowOrderService assetBorrowOrderService;

    @ApiOperation(value = "检查版本号")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "version", value = "当前版本号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "eq_type", value = "设备类型1安卓2ios", dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/chk_version")
    public void chk_version(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getParameters(request);
        JSONObject return_params = new JSONObject();
        String version_db = "";
        String url =null;
        Map<String, String> mapVersion = backConfigParamsService.getBackConfig(BackConfigParams.VERSION, null);
                
        if(params.get("eq_type").equals("1")){
        	version_db = mapVersion.get("version_android");
            url = mapVersion.get("update_android_url");
        }else if(params.get("eq_type").equals("2")){
        	version_db = mapVersion.get("version_ios");
            url = mapVersion.get("update_ios_url");
        }
        
        int v_db = Integer.valueOf(version_db.replace(".", "")) ;
        int v_local = Integer.valueOf(params.get("version").replace(".", ""));
        
        if(v_db>v_local){

        	if(mapVersion.get("need_update").equals("1")){
        		return_params.put("need_update", "1");
        	}else{
        		return_params.put("need_update", "0");
        	}
        	
        	return_params.put("suggest_update", "1");
        	return_params.put("update_url", url);
        }else{
        	return_params.put("suggest_update", "0");
        	return_params.put("need_update", "0");
        }
        
        
        JSONObject json = new JSONObject();
        json.put("result", return_params);
        json.put("code", "0");
        json.put("message", "版本查询成功");
        
        JsonUtils.toJson(response, json);
    }
    
    
    @RequestMapping(value = "/down_new_version")
    public ModelAndView down_new_version(HttpServletRequest request, HttpServletResponse response) {
    	
    	ModelAndView mav = new ModelAndView();
    	
        Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.CHANNEL, null);
        String androidUrl = mapFee.get("android_down_url");
        String iosUrl = mapFee.get("ios_down_url");

        mav.addObject("androidUrl", androidUrl);
        mav.addObject("iosUrl", iosUrl);
    	
    	
    	mav.setViewName("tg/newversion_download");
    	return mav;
    	
    }
    
    
    @RequestMapping(value = "/guide")
    public ModelAndView iosguide(HttpServletRequest request, HttpServletResponse response) {
    	
    	ModelAndView mav = new ModelAndView();
    	
    	mav.setViewName("tg/guide");
    	return mav;
    	
    }
    

}
