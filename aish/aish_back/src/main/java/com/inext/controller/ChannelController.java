package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.inext.constants.Constants;
import com.inext.entity.BackConfigParams;
import com.inext.entity.ChannelBalanceStatistics;
import com.inext.entity.ChannelInfo;
import com.inext.entity.ChannelLoanQuota;
import com.inext.service.IChannelService;
import com.inext.utils.DateUtil;
import com.inext.utils.ExcelUtil;
import com.inext.utils.JsonUtils;
import com.inext.utils.StringUtils;
import com.inext.utils.encrypt.AESUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 渠道
 */
@Controller
@RequestMapping("channel")
public class ChannelController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private IChannelService channelService;

    /**
     * 渠道列表 商户下的所有渠道列表
     *
     * @param request
     * @param model
     * @return
     */


    @RequestMapping("/getChannelList")
    public String getChannelList(HttpServletRequest request, Model model) {
        try {
            Map<String, Object> params = this.getParametersO(request);
            if (null != params.get("channelName")) {
                model.addAttribute("channelName", params.get("channelName").toString());
            }
            if (null != params.get("channelCode")) {
                model.addAttribute("channelCode", params.get("channelCode").toString());
            }
            PageInfo<ChannelInfo> list = channelService.getPageList(params);
            model.addAttribute("pageInfo", list);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/channel/channelList";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response, ChannelInfo channel) {

        String msg = "";
        String code = "";
        try {
            Date date = new Date();
            ChannelLoanQuota channelLoanQuota= new ChannelLoanQuota();
            channelLoanQuota.setLoanPer(request.getParameter("loanPer"));
            channelLoanQuota.setLoanWy(request.getParameter("loanWy"));
            channelLoanQuota.setLoanAll(request.getParameter("loanAll"));
            if (null == channel.getId()) {

                Map<String, String> map2 = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CHANNEL);
                String channelTgUrl = map2.get("CHANNEL_PROMOTION");

                String channel_url = channelTgUrl + "?code=" + AESUtil.encrypt(channel.getChannelCode(), "");
                channel.setChannelUrl(channel_url);
                channel.setCreateTime(date);
                channel.setUpdateTime(date);
                channelService.insert(channel);

                //查询
                ChannelLoanQuota old= channelService.getChannelLoanQuotaByChannelId(channel.getId());
                if(old == null){
                    channelLoanQuota.setChannelId(channel.getId());
                    channelLoanQuota.setCreateTime(date);
                    channelLoanQuota.setUpdateTime(date);
                    channelService.insertChannelLoanQuota(channelLoanQuota);//新增
                }
            } else {
                channelService.updateById(channel);
                //查询
                ChannelLoanQuota old= channelService.getChannelLoanQuotaByChannelId(channel.getId());
                if(old == null || old.getId() == null || old.getChannelId() == null){
                    channelLoanQuota.setChannelId(channel.getId());
                    channelLoanQuota.setCreateTime(date);
                    channelLoanQuota.setUpdateTime(date);
                    //新增
                    channelService.insertChannelLoanQuota(channelLoanQuota);
                }else{
                    old.setLoanPer(channelLoanQuota.getLoanPer());
                    old.setLoanWy(channelLoanQuota.getLoanWy());
                    old.setLoanAll(channelLoanQuota.getLoanAll());
                    old.setUpdateTime(date);
                    //更新
                    channelService.updateChannelLoanQuota(old);
                }
            }
            code = "200";
            msg = "保存成功";
        } catch (Exception e) {
            logger.error("back  error ", e);
            code = "-1";
            msg = "系统错误";
            if (e.getLocalizedMessage().indexOf("UK_channel_code") >= 0) {
                msg = "渠道码重复！";
            }

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
            ChannelInfo channel = new ChannelInfo();
            ChannelLoanQuota channelLoanQuota = null;
            if (!StringUtil.isEmpty(id)) {
                //修改
                channel = channelService.getChannelById(Integer.parseInt(id));
                //查询渠道借款额度记录
                channelLoanQuota = channelService.getChannelLoanQuotaByChannelId(NumberUtils.toInt(id));
            }
            model.addAttribute("channel", channel);
            model.addAttribute("channelLoanQuota", channelLoanQuota!=null?channelLoanQuota: new ChannelLoanQuota());
        } catch (Exception e) {
            logger.error("back  error ", e);
        }
        return "v2/channel/channelSave";
    }


    @RequestMapping("channelListExport")
    public void channelListExport(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            Map<String, Object> param = super.getParametersO(request);
            List<ChannelInfo> channelList = channelService.getList(param);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (channelList != null && channelList.size() > 0) {
                for (ChannelInfo channel : channelList) {
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("id", channel.getId());
                    temp.put("channelName", channel.getChannelName());
                    temp.put("channelCode", channel.getChannelCode());
                    temp.put("oldUserScore", channel.getOldUserScore());
                    temp.put("newUserScore", channel.getNewUserScore());
                    temp.put("channelUrl", channel.getChannelUrl());
                    temp.put("createTime", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", channel.getCreateTime()));
                    list.add(temp);
                }
            }

            if (list != null && list.size() > 0) {
                Map<String, String> title = new LinkedHashMap<>();
                title.put("channelName", "渠道名称");
                title.put("channelCode", "渠道码");
                title.put("channelUrl", "渠道链接");
                title.put("oldUserScore", "渠道老用户信用分");
                title.put("newUserScore", "渠道新用户信用分");
                title.put("createTime", "创建时间");
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

    /**
     * 渠道统计
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("getChannelStatistics")
    public String getChannelStatistics(HttpServletRequest request, HttpServletResponse response, Model model) {


        try {
            Map<String, Object> params = this.getParametersO(request);
            if (null != params.get("channelName")) {
                model.addAttribute("channelName", params.get("channelName").toString());
            }
            if (null != params.get("channelCode")) {
                model.addAttribute("channelCode", params.get("channelCode").toString());
            }
            if (params.get("statisticsTime") == null) {
                params.put("statisticsTime", DateUtil.getDateFormat("yyyy-MM-dd"));
                model.addAttribute("statisticsTime", DateUtil.getDateFormat("yyyy-MM-dd"));
            } else {
                model.addAttribute("statisticsTime", params.get("statisticsTime"));
            }
            if (null != params.get("sortColumn"))
                model.addAttribute("sortColumn", params.get("sortColumn"));

            PageInfo<Map<String, Object>> list = channelService.findStatisticsPage(params);
            model.addAttribute("pageInfo", list);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/channel/channelStatistics";

    }

    /**
     * 渠道结算统计
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("getChannelBalanceStatistics")
    public String getChannelBalanceStatistics(HttpServletRequest request, HttpServletResponse response, Model model) {


        try {
            Map<String, Object> params = this.getParametersO(request);
            if (null != params.get("channelName")) {
                model.addAttribute("channelName", params.get("channelName").toString());
            }
            if (null != params.get("channelCode")) {
                model.addAttribute("channelCode", params.get("channelCode").toString());
            }
            if (null != params.get("startDate")) {
                model.addAttribute("startDate", params.get("startDate").toString());
            }
            if (null != params.get("endDate")) {
                model.addAttribute("endDate", params.get("endDate").toString());
            }
            if (null != params.get("sortColumn"))
                model.addAttribute("sortColumn", params.get("sortColumn"));

            PageInfo<ChannelBalanceStatistics> list = channelService.findBalanceStatisticsPage(params);
            Map<String, Object> balanceInfo = channelService.findBalanceStatistics(params);
            model.addAttribute("pageInfo", list);
            model.addAttribute("balanceInfo", balanceInfo);

        } catch (Exception e) {
            logger.error("back  error ", e);
        }

        return "v2/channel/channelBalanceStatistics";

    }


    private String getExlName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date()) + System.currentTimeMillis() + ".xls";
    }

    @RequestMapping("channelStatisticsExcel")
    public void channelStatisticsExcel(HttpServletResponse response, HttpServletRequest request) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            Map<String, Object> param = super.getParametersO(request);
            List<Map<String, Object>> channelList = channelService.getStatisticsList(param);


            if (channelList != null && channelList.size() > 0) {
                Map<String, String> title = new LinkedHashMap<>();
                title.put("channelName", "渠道名称");
                title.put("channelCode", "渠道码");
                title.put("createTime", "日期");
                title.put("pvCount", "PV");
                title.put("uvCount", "UV");
                title.put("reUvRate", "UV-注册转换率");
                title.put("registerCount", "注册人数");
                title.put("nameCount", "实名认证");
                title.put("contactsCount", "手机通讯录");
                title.put("operatorCount", "运营商认证");
                title.put("cardCount", "绑定银行卡");
                title.put("passwordCount", "设置支付密码");
                title.put("approveCount", "审核通过");
                title.put("loanCount", "已放款");
                ExcelUtil.writeWorkbook(out, channelList, title);
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

    @RequestMapping("channelBalanceStatisticsExcel")
    public void channelBalanceStatisticsExcel(HttpServletResponse response, HttpServletRequest request) {
        try (ServletOutputStream out = response.getOutputStream()) {
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            Map<String, Object> param = super.getParametersO(request);
            List<ChannelBalanceStatistics> channelList = channelService.channelBalanceStatisticsExcel(param);


            List<Map<String, Object>> list = new ArrayList<>();
            if (channelList.size() > 0) {
                for (ChannelBalanceStatistics channel : channelList) {
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("statisticsDate", DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", channel.getStatisticsDate()));
                    temp.put("channelName", channel.getChannelName());
                    temp.put("channelCode", channel.getChannelCode());
                    temp.put("pvCount", channel.getPvCount());
                    temp.put("uvCount", channel.getUvCount());
                    temp.put("reUvRate", channel.getReUvRate());
                    temp.put("registerCount", channel.getRegisterCount());
                    temp.put("applyCount", channel.getApplyCount());
                    temp.put("newUserCount", channel.getNewUserCount());
                    temp.put("oldUserCount", channel.getOldUserCount());
                    temp.put("loanCount", channel.getLoanCount());
                    temp.put("newLoanCount", channel.getNewLoanCount());
                    temp.put("oldLoanCount", channel.getOldLoanCount());
                    temp.put("loanMoneyMount", channel.getLoanMoneyMount());
                    list.add(temp);
                }
            }

            if (list.size() > 0) {
                Map<String, String> title = new LinkedHashMap<>();
                title.put("statisticsDate", "日期");
                title.put("channelName", "渠道名称");
                title.put("channelCode", "渠道码");
                title.put("pvCount", "pv");
                title.put("uvCount", "uv");
                title.put("reUvRate", "UV-注册转换率");
                title.put("registerCount", "注册人数");
                title.put("applyCount", "申请人数");
                title.put("newUserCount", "新用户申请人数");
                title.put("oldUserCount", "老用户申请人数");
                title.put("loanCount", "放款人数");
                title.put("newLoanCount", "新用户放款人数");
                title.put("oldLoanCount", "老用户放款人数");
                title.put("loanMoneyMount", "放款总额");
                ExcelUtil.writeWorkbook(out, list, title);
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
