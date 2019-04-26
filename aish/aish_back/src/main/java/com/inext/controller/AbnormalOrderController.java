package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.dao.IBankAllInfoDao;
import com.inext.entity.AbnormalOrder;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.BackUser;
import com.inext.entity.BankAllInfo;
import com.inext.enumerate.Status;
import com.inext.result.AjaxResult;
import com.inext.service.IAbonormalOrderService;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenkk on 2018/3/21 09：31. 还款订单
 */
@Controller
@RequestMapping("abnormalOrder")
public class AbnormalOrderController extends BaseController {
    Logger logger = LoggerFactory.getLogger(AbnormalOrderController.class);

    @Resource
    private IAbonormalOrderService iAbonormalOrderService;
    @Resource
    private IBankAllInfoDao bankAllInfoDao;
    @Resource
    private IAssetRepaymentOrderService iAssetRepaymentOrderService;

    /**
     * 异常订单查询
     */
    @RequestMapping("pageList")
    public ModelAndView getPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        PageInfo<AssetRepaymentOrder> page = iAbonormalOrderService.getPageList(params);
        List<BankAllInfo> bankInfo = bankAllInfoDao.findBankAllInfo();
        request.setAttribute("pageInfo", page);
        request.setAttribute("bankAllInfo", bankInfo);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userId", params.get("userId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("creditRepaymentTime", params.get("creditRepaymentTime"));
        mav.addObject("repaymentTime", params.get("repaymentTime"));
        mav.addObject("bankName", params.get("bankName"));
        mav.addObject("cardCode", params.get("cardCode"));
        mav.addObject("dkPayStatus", params.get("dkPayStatus"));
        mav.addObject("seqId", params.get("seqId"));
        mav.addObject("repaymentRealTime", params.get("repaymentRealTime"));
        mav.addObject("hlOrderNo", params.get("hlOrderNo"));
        mav.addObject("hlTime", params.get("hlTime"));
        mav.addObject("hcOrderNo", params.get("hcOrderNo"));
        mav.addObject("hcxTime", params.get("hcxTime"));
        mav.addObject("creatTime", params.get("creatTime"));
        mav.addObject("creater", params.get("creater"));
        mav.addObject("type", params.get("type"));
        mav.addObject("refundOrderNo", params.get("refundOrderNo"));
        mav.addObject("refundBank", params.get("refundBank"));
        mav.addObject("refundCardNo", params.get("refundCardNo"));
        mav.addObject("amount", params.get("amount"));
        mav.addObject("remark", params.get("remark"));
        mav.addObject("id", params.get("id"));
        mav.addObject("bankName", params.get("bankName"));
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("searchOrderId", params.get("searchOrderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("searchType", params.get("type"));
        mav.setViewName("v2/abnormalOrder/abnormalOrderList");
        return mav;
    }

    @RequestMapping(value = "/saveAbnormal")
    @ResponseBody
    public AjaxResult saveAbnormal(HttpServletRequest request, HttpServletResponse response, AbnormalOrder abnormalOrder) {
        BackUser user = loginAdminUser(request);
        try {
            abnormalOrder.setCreatTime(new Date());
            abnormalOrder.setOperator(String.valueOf(user.getId()));
            if (abnormalOrder.getRepaymentChannel() == 1) {
                int orderId = abnormalOrder.getOrderId();
                int refundAmount = Integer.parseInt(abnormalOrder.getAmount());
                int result = iAssetRepaymentOrderService.updateCeditAndRepaymented(orderId, refundAmount);
                if (result == -1) {
                    return new AjaxResult("false", "退款金额不能大于已还金额");
                }
            }
            iAbonormalOrderService.saveAbnormal(abnormalOrder);
        } catch (Exception e) {
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        }

        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }

    /**
     * 异常订单 导出
     */
    @RequestMapping("pageListExport")
    public void pageListExport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iAbonormalOrderService.getAbnormalList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("order_id", "订单号");
            title.put("user_name", "姓名");
            title.put("user_phone", "手机号");
            title.put("credit_repayment_time", "借款日期");
            title.put("repayment_time", "应还款日期");
            title.put("bank_name", "绑定银行");
            title.put("cardCode", "绑定银行卡号");
            title.put("seq_id", "代扣商家单号");
            title.put("repayment_real_time", "代扣时间");
            title.put("hlOrderNo", "快钱单号");
            title.put("hlTime", "快钱扣款时间");
            title.put("hcOrderNo", "汇潮单号");
            title.put("hcxtime", "汇潮扣款时间");
            title.put("issue", "异常");
            title.put("refund_order_no", "退款单号");
            title.put("refundBankName", "退款银行");
            title.put("amount", "退款金额(元)");
            title.put("refund_card_no", "退款银行账号");
            title.put("remark", "备注");
            title.put("nick_name", "处理人");
            title.put("creat_time", "处理完成时间");
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

    private String getExlName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date()) + System.currentTimeMillis() + ".xls";
    }

}
