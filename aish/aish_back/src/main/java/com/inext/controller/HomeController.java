package com.inext.controller;

import com.inext.entity.HomeInfo;
import com.inext.service.IHomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class HomeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Resource
    private IHomeService homeService;

    @RequestMapping(value = "/home")
    public String home(HttpServletRequest request, HttpServletResponse response, Model model) {
        HomeInfo homeInfo = homeService.getAllCount();
        model.addAttribute("homeInfo", homeInfo);
        return "v2/home";
    }

}
