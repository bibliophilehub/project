package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.inext.entity.BannerInfo;
import com.inext.service.IBannerInfoService;
import com.inext.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping(value = "/banner")
public class BannerController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BannerController.class);
    @Resource
    private IBannerInfoService iBannerInfoService;

    @RequestMapping("/bannerList")
    public String bannerList(HttpServletRequest request, Model model) {

        try {
            Map<String, Object> params = this.getParametersO(request);
            PageInfo<BannerInfo> bannerList = iBannerInfoService.getPageList(params);
            model.addAttribute("pageInfo", bannerList);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/banner/bannerList";
    }

    @RequestMapping("/to-save")
    public String toSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String id = request.getParameter("id");
            BannerInfo bannerInfo = new BannerInfo();
            if (!StringUtil.isEmpty(id)) {
                //修改
                bannerInfo = iBannerInfoService.getBannerById(Integer.parseInt(id));
            }

            model.addAttribute("bannerInfo", bannerInfo);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/banner/bannerSave";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response, BannerInfo bannerInfo) {

        try {
            if (null == bannerInfo.getId()) {
                bannerInfo.setCreateTime(new Date());
                iBannerInfoService.saveBanner(bannerInfo);
            } else {
                iBannerInfoService.updateBanner(bannerInfo);

            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "保存成功");
            JsonUtils.toObjectJson(response, jsonObject);
        } catch (Exception e) {
            logger.error("back  error ", e);
        }
    }
}
