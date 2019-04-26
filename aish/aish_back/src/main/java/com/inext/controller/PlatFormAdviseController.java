package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.entity.PlatfromAdvise;
import com.inext.service.IAdviseService;
import com.inext.utils.DateUtil;
import com.inext.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "/advise")
public class PlatFormAdviseController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PlatFormAdviseController.class);
    @Resource
    private IAdviseService iAdviseService;

    @RequestMapping("/adviseList")
    public String bannerList(HttpServletRequest request, Model model) {
        try {
            Map<String, Object> params = this.getParametersO(request);
            if (null != params.get("userPhone")) {

                model.addAttribute("userPhone", params.get("userPhone").toString());
            }
            PageInfo<PlatfromAdvise> list = iAdviseService.getPageList(params);
            model.addAttribute("pageInfo", list);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/advise/adviseList";
    }


    @RequestMapping("adviseExport")
    public void adviseExport(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            Map<String, Object> param = super.getParametersO(request);
            List<PlatfromAdvise> adviseList = iAdviseService.getList(param);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (adviseList != null && adviseList.size() > 0) {
                for (PlatfromAdvise ad : adviseList) {
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("id", ad.getId());
                    temp.put("adviseContent", ad.getAdviseContent());
                    temp.put("adviseAddtime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", ad.getAdviseAddtime()));
                    temp.put("userPhone", ad.getUserPhone());
                    list.add(temp);
                }
            }

            if (list != null && list.size() > 0) {
                Map<String, String> title = new LinkedHashMap<>();
                title.put("adviseContent", "建议内容");
                title.put("userPhone", "手机号码");
                title.put("adviseAddtime", "添加时间");
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

    private String getExlName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date()) + System.currentTimeMillis() + ".xls";
    }
}
