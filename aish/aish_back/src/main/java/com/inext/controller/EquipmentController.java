package com.inext.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.inext.entity.EquipmentInfo;
import com.inext.service.IEquipmentService;
import com.inext.utils.JsonUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("equipment")
public class EquipmentController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private IEquipmentService equipmentService;



    @RequestMapping("/getEquipmentList")
    public String getEquipmentList(HttpServletRequest request, Model model) {
        try {
        	Map<String, Object> params = this.getParametersO(request);
            PageInfo<EquipmentInfo> list = equipmentService.getPageList(params);
            model.addAttribute("pageInfo", list);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/equipment/equipmentList";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response, EquipmentInfo equipmentInfo) {

        String msg = "";
        String code = "";
        try {
            Date date = new Date();
            if (null == equipmentInfo.getId()) {

                equipmentInfo.setCreateTime(date);
                equipmentService.insert(equipmentInfo);

            } else {
            	equipmentService.updateById(equipmentInfo);
            }
            code = "200";
            msg = "操作成功";
        } catch (Exception e) {
            logger.error("back  error ", e);
            code = "-1";
            msg = "系统错误";
        } finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            jsonObject.put("msg", msg);
            JsonUtils.toObjectJson(response, jsonObject);
        }
    }
    @RequestMapping("/del")
    public void del(HttpServletRequest request, HttpServletResponse response,String id) {
    	
    	String msg = "";
    	String code = "";
    	try {
    		equipmentService.delById(id);
    		code = "200";
    		msg = "保存成功";
    	} catch (Exception e) {
    		logger.error("back  error ", e);
    		code = "-1";
    		msg = "系统错误";
    	} finally {
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("code", code);
    		jsonObject.put("msg", msg);
    		JsonUtils.toObjectJson(response, jsonObject);
    	}
    }

    @RequestMapping("/to-save")
    public String toSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String id = request.getParameter("id");
            EquipmentInfo equipmentInfo = new EquipmentInfo();
            if (!StringUtil.isEmpty(id)) {
                //修改
            	equipmentInfo = equipmentService.getEquipmentById(id);
            }

            model.addAttribute("equipmentInfo", equipmentInfo);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/equipment/equipmentSave";
    }






}
