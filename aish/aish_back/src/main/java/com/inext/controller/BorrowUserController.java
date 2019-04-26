package com.inext.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stylefeng.guns.modular.system.model.BorrowUserAllInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.inext.service.IBorrowUserService;
import com.inext.service.IChannelService;
import com.inext.utils.ExcelUtil;
import com.inext.utils.JsonUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户管理
 *
 * @author ttj
 */
@Controller
@RequestMapping(value = "/borrowUser")
public class BorrowUserController {
    private static Logger logger = LoggerFactory.getLogger(BorrowUserController.class);

    @Resource
    IBorrowUserService borrowUserService;
    @Resource
    private IChannelService channelService;

    /**
     * 用户管理列表查询
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryBorrowUser")
    public String queryBorrowUser(HttpServletRequest request, HttpServletResponse response) {
        borrowUserService.queryBorrowUser(request);
        return "v2/borrowUser/borrowUserIndex";
    }

    /**
     * 用户管理列表查询
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateBorrowUserIsBlack")
    public void updateBorrowUserIsBlack(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            borrowUserService.updateBorrowUserIsBlack(request);
            json.put("status", 0);
            json.put("msg", "设置成功!");
        } catch (Exception e) {
            json.put("status", 1);
            json.put("msg", "设置失败!");
        }
        JsonUtils.toJson(response, json);
    }

    /**
     * 导出注册用户数据
     *
     * @param response
     * @param startDate
     * @param endDate
     * @param channelId
     */
    @RequestMapping("exportBorroUserExcelFile")
    public void exportBorroUserExcelFile(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("注册用户信息.xls".getBytes("GB2312"), "iso8859-1"));
            List<Map<String, String>> list = borrowUserService.exportBorroUserExcelFile(request);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("id", "用户Id");
            title.put("userPhone", "手机号");
            title.put("userName", "姓名");
            title.put("userCardNo", "身份证号");
            title.put("isVerified", "实名认证");
            title.put("isPhone", "手机通讯录");
            title.put("isOperator", "运营商认证");
            title.put("isCard", "绑定银行卡");
            title.put("transactionPassword", "设置支付密码");
            title.put("isBlack", "黑名单");
            title.put("score" , "风控分");
            title.put("detail", "风控消息");
            title.put("createTime", "注册时间");
            title.put("isOld", "老用户");
            title.put("channelName", "渠道");
            ExcelUtil.writableWorkbook(out, list, title);
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

    /**
     * 用户管理列表查询
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/borrowUserDetail")
    public String borrowUserDetail(HttpServletRequest request, HttpServletResponse response) {
        borrowUserService.borrowUserDetail(request);
        String isCollection=request.getParameter("isCollectin");
        if(null != isCollection && "1".equals(isCollection))
            return "v2/borrowUser/borrowUserDetailForCollection";
        return "v2/borrowUser/borrowUserDetail";
    }


    /**
     * 催收获取客户信息
     * @param userId
     * @param orderId
     * @return
     */
    @GetMapping(value="/getuserAllInfo")
    @ResponseBody
    public Object getUserAllInfo(@RequestParam(value = "userId",required = false) Integer userId, @RequestParam(value = "orderId",required = false) Integer orderId){
        try{

            return this.borrowUserService.getUserAllInfo(userId,orderId);
        }catch (Exception e){
            e.printStackTrace();
            BorrowUserAllInfoDto borrowUserAllInfoDto = new BorrowUserAllInfoDto();
            borrowUserAllInfoDto.setCode(500);
            return borrowUserAllInfoDto;
        }
    }

}
