package com.inext.controller;

import com.inext.entity.BackUser;
import com.inext.service.ISupermarketInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping
public class IndexController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    ISupermarketInfoService iSupermarketInfoService;

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request,
                        HttpServletResponse response, Model model) {
        logger.info("IndexController index");
        BackUser user = loginAdminUser(request);
        model.addAttribute("backUser", user);

        //区分是否是导流渠道商
        if("02".equals(user.getUserType())) {
            return "v2/diversion/diversionIndex";
        } else {
            return "v2/index";
        }
    }

    @RequestMapping(value = "/left")
    public String left(HttpServletRequest request,
                       HttpServletResponse response, Model model) {
        logger.info("IndexController left");
        return "left";
    }
    @RequestMapping(value = "/dc_index")
    public String dc_index(HttpServletRequest request,
                        HttpServletResponse response, Model model) {
        logger.info("IndexController dc_index");
        model.addAttribute("list", iSupermarketInfoService.getShowList());
        return "dc_index";
    }
}
