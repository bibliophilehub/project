package com.inext.controller;


import com.github.pagehelper.util.StringUtil;
import com.inext.entity.SupermarketInfo;
import com.inext.service.ISupermarketInfoService;
import com.sun.mail.imap.protocol.ID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/supermarket")
public class SupermarketController extends BaseController {

    @Resource
    ISupermarketInfoService iSupermarketInfoService;


    @RequestMapping("/list")
    public String orderList(HttpServletRequest request, Model model,SupermarketInfo info) {

        model.addAttribute("pageInfo", iSupermarketInfoService.getList(info));
        return "v2/supermarket/list";
    }


    @RequestMapping("/detail/{id}")
    public String detail(HttpServletRequest request, Model model,@PathVariable("id") int id) {
        model.addAttribute("info", iSupermarketInfoService.getDataById(id));
        return "v2/supermarket/save";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Map add(HttpServletRequest request, SupermarketInfo info, Model model) {
        Map map=new HashMap();
        int code=0;
        String msg="保存失败";
        if(info.getId()!=null){
            code=iSupermarketInfoService.doChg(info);
        }else {
            code=iSupermarketInfoService.doAdd(info);
        }
        if(code!=0){
            msg="保存成功";
        }
        map.put("code",code+"");
        map.put("msg",msg);
        return map;
    }

    @RequestMapping("/del/{id}")
    public String del(HttpServletRequest request, Model model,@PathVariable("id") int id) {
        iSupermarketInfoService.doDel(id);
        return orderList(request,model,new SupermarketInfo());
    }

    @RequestMapping("/to-save")
    public String toSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String id = request.getParameter("id");
            SupermarketInfo supermarketinfo = new SupermarketInfo();
            if (!StringUtil.isEmpty(id)) {
                //修改
                supermarketinfo = iSupermarketInfoService.getDataById(Integer.getInteger(id));
            }
            model.addAttribute("info", supermarketinfo);

        } catch (Exception e) {
        }
        return "v2/supermarket/save";
    }

    @RequestMapping("/cli/{id}")
    @ResponseBody
    public void getPageList(@PathVariable("id") Integer id) {
        if(id!=null&&id>0){
            iSupermarketInfoService.doclick(id);
        }
    }
}
