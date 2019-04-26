package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.dao.IBankAllInfoDao;
import com.inext.entity.*;
import com.inext.enumerate.Status;
import com.inext.result.AjaxResult;
import com.inext.result.ApiServiceResult;
import com.inext.service.IAssetOrderStatusHisService;
import com.inext.service.IAssetRenewalOrderService;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.service.ICapitalFlowOrderService;
import com.inext.service.pay.impl.HcServiceImpl;
import com.inext.utils.ExcelUtil;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenkk on 2018/3/21 09：31. 还款订单
 */
@Controller
@RequestMapping("/capitalFlowOrder")
public class CapitalFlowOrderController extends BaseController {
    Logger logger = LoggerFactory.getLogger(CapitalFlowOrderController.class);
    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;
    @Resource
    ICapitalFlowOrderService iCapitalFlowOrderService;
    @Autowired
    IAssetRenewalOrderService iAssetRenewalOrderService;
    @Autowired
    IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Autowired
    RedisUtil redisUtil;
    @Resource
    private IBankAllInfoDao bankAllInfoDao;
    @Autowired
    private HcServiceImpl hcService;

    /**
     * 流水记录查询
     */
    @RequestMapping("/pageList")
    public ModelAndView getPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        BackUser user = loginAdminUser(request);
        Map<String, Object> params = this.getParametersO(request);
        if (null == params.get("startDate") || null == params.get("endDate") || "".equals(params.get("startDate")) || "".equals(params.get("endDate"))) {
            Map<Integer, String> dateMap = getInitQueryStartDate(true);
            params.put("startDate", dateMap.get(1));
            params.put("endDate", dateMap.get(2));
        }
        PageInfo<AssetRepaymentOrder> page = null;
        if (null != params.get("init")) {
            page = getPage();
        } else
            page = iCapitalFlowOrderService.getFlowPageList(params);
        List<BankAllInfo> bankInfo = bankAllInfoDao.findBankAllInfo();
        request.setAttribute("user", user);
        request.setAttribute("pageInfo", page);
        request.setAttribute("bankAllInfo", bankInfo);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("userId", params.get("userId"));
        mav.addObject("moneyAmount", params.get("moneyAmount"));
        mav.addObject("penaltyAmount", params.get("penaltyAmount"));
        mav.addObject("planLateFee", params.get("planLateFee"));
        mav.addObject("repaymentAmount", params.get("repaymentAmount"));
        mav.addObject("ceditAmount", params.get("ceditAmount"));
        mav.addObject("repaymentedAmount", params.get("repaymentedAmount"));
        mav.addObject("noOrder", params.get("noOrder"));
        mav.addObject("cardCode", params.get("cardCode"));
        mav.addObject("updateTime", params.get("updateTime"));
        mav.addObject("repaymentTime", params.get("repaymentTime"));
        mav.addObject("paymentChannel", params.get("paymentChannel"));//放款渠道
        mav.addObject("repaymentType", params.get("repaymentType"));//还款方式
        mav.addObject("repaymentChannel", params.get("repaymentChannel"));//还款渠道
        mav.addObject("type", params.get("type"));
        mav.addObject("payType", params.get("payType"));
        mav.addObject("refundOrderNo", params.get("refundOrderNo"));
        mav.addObject("refundChannel", params.get("refundChannel"));
        mav.addObject("refundBank", params.get("refundBank"));
        mav.addObject("refundCardNo", params.get("refundCardNo"));
        mav.addObject("amount", params.get("amount"));
        mav.addObject("remark", params.get("remark"));
        mav.addObject("bankName", params.get("bankName"));
        mav.addObject("creatTime", params.get("creatTime"));
        mav.addObject("nickName", params.get("nickName"));
        mav.addObject("issue", params.get("issue"));
        mav.addObject("paymentChannels", PaymentChannel.DESCRIPTION);
        mav.addObject("repaymentTypeMap", AssetRepaymentOrder.repaymentTypeMap);
        mav.addObject("repaymentChannelMap", AssetRepaymentOrder.repaymentChannelMap);
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("searchOrderId", params.get("searchOrderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("searchType", params.get("searchType"));
        mav.addObject("refundNoOrder", params.get("refundNoOrder"));

        mav.setViewName("v2/capitalFlow/capitalFlowOrderList");
        return mav;
    }

    /**
     * 流水列表导出
     */
    @RequestMapping("capitalFlowOrderPageListExport")
    public void capitalFlowPageList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iCapitalFlowOrderService.getCapitalFlowList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("orderId", "订单号");
            title.put("userName", "姓名");
            title.put("userPhone", "手机号");
            title.put("moneyAmount", "借款金额");
            title.put("penalty_amount", "违约金");
            title.put("plan_late_fee", "滞纳金");
            title.put("repayment_amount", "累计应还金额");
            title.put("cedit_amount", "累计减免金额");
            title.put("repaymentedAmount", "还款金额");
            title.put("cumulative_repayment_amount", "累计已款金额");
            title.put("payType", "还款类型");
            title.put("paymentChannel", "资方");
            title.put("noOrder", "还款单号");
            title.put("repaymentType", "还款方式");
            title.put("repaymentChannel", "还款渠道");
            title.put("updateTime", "还款时间");
//            title.put("refund_channel", "退款渠道");
//            title.put("refundBankName", "退款银行");
            title.put("amount", "退款金额");
//            title.put("refund_order_no", "退款电子回执单号");
//            title.put("refund_card_no", "退款卡号");
//            title.put("issue", "退款原因");
//            title.put("nickName", "退款操作人");
            title.put("creatTime", "退款时间");
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
     * 已退款列表查询
     */
    @RequestMapping("/refundOrderPageList")
    public ModelAndView getAbnormalPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        if (null == params.get("startDate") || null == params.get("endDate") || "".equals(params.get("startDate")) || "".equals(params.get("endDate"))) {
            Map<Integer, String> dateMap = getInitQueryStartDate(true);
            params.put("startDate", dateMap.get(1));
            params.put("endDate", dateMap.get(2));
        }
        PageInfo<AbnormalOrder> page = null;
        if (null != params.get("init")) {
            page = new PageInfo<>(null);
        } else
            page = iCapitalFlowOrderService.getAbnormalOrder(params);
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("repaymentMoney", params.get("repaymentMoney"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("refundOrderNo", params.get("refundOrderNo"));
        mav.addObject("refundCardNo", params.get("refundCardNo"));
        mav.addObject("amount", params.get("amount"));
        mav.addObject("remark", params.get("remark"));
        mav.addObject("refundChannel", params.get("refundChannel"));
        mav.addObject("refundBank", params.get("refundBank"));
        mav.addObject("issue", params.get("issue"));
        mav.addObject("nickName", params.get("nickName"));
        mav.addObject("creatTime", params.get("creatTime"));
        mav.addObject("refundIssueMap", AbnormalOrder.refundIssueMap);
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("searchOrderId", params.get("searchOrderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.setViewName("v2/capitalFlow/refundOrderList");
        return mav;
    }


    /**
     * 已退款列表导出
     */
    @RequestMapping("refundOrderPageListExport")
    public void repaymentedPageList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iCapitalFlowOrderService.getList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("order_id", "订单号");
            title.put("user_name", "姓名");
            title.put("userPhone", "手机号");
            title.put("repayment_money", "收款金额");
            title.put("repayment_order_no", "收款单号");
            title.put("amount", "退款金额");
            title.put("refund_channel", "退款渠道");
            title.put("refundBank", "退款银行");
            title.put("refund_order_no", "退款电子回执单号");
            title.put("refund_card_no", "退款账号");
            title.put("issue", "退款原因");
            title.put("nick_name", "退款操作人");
            title.put("creat_time", "退款时间");
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

    /**
     * 保存操作线下还款数据
     *
     * @param assetRepaymentDetail
     * @return
     */
    @RequestMapping(value = "/saveAssetRepaymentDetail")
    @ResponseBody
    public AjaxResult saveAssetRepaymentDetail(HttpServletRequest request, HttpServletResponse response, AssetRepaymentDetail assetRepaymentDetail) {
        BackUser user = loginAdminUser(request);
        try {
            assetRepaymentDetail.setRepaymentChannel(2);
            assetRepaymentDetail.setCreatedAt(new Date());
            assetRepaymentDetail.setAdminUsername(user.getAccount());
            assetRepaymentDetail.setType(1);
            //进行收款单号唯一性校验
            String repaymentOrderNo = assetRepaymentDetail.getRepaymentOrderNo();
            Long count = iCapitalFlowOrderService.getARDetailByRepaymentOrderNo(repaymentOrderNo);
            if (count > 0) {
                return new AjaxResult(Status.FAIL.getName(), "还款单号重复，请核对后进行填写");
            }

//            AssetRepaymentOrder aroModel = iAssetRepaymentOrderService.getRepaymentByOrderId(assetRepaymentDetail.getOrderId());
//            aroModel.setRepaymentedAmount(aroModel.getRepaymentedAmount().add(assetRepaymentDetail.getTrueRepaymentMoney()));
//            aroModel.setCeditAmount(aroModel.getCeditAmount().add(assetRepaymentDetail.getCeditAmount()));

            iCapitalFlowOrderService.saveAssetRepaymentDetail(assetRepaymentDetail);
        } catch (Exception e) {
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        }

        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }


    /**
     * 保存操作退款数据
     *
     * @param abnormalOrder
     * @return
     */
    @RequestMapping(value = "/saveAbnormal")
    @ResponseBody
    public AjaxResult saveAbnormal(HttpServletRequest request, HttpServletResponse response, AbnormalOrder abnormalOrder) {
        BackUser user = loginAdminUser(request);
        AjaxResult ajaxResult = new AjaxResult();
        try {
            abnormalOrder.setCreatTime(new Date());
            abnormalOrder.setOperator(String.valueOf(user.getId()));
            ajaxResult = iCapitalFlowOrderService.saveAbnormal(abnormalOrder);
        } catch (Exception e) {
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        }

        return ajaxResult;
    }

    /**
     * 一麻袋提现
     */
    @RequestMapping("/yiMaDaiPage")
    public ModelAndView getYiMaDaiPage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        if (null != params.get("status")) {
            params.put("status", params.get("status"));
        }
        if (null != params.get("orderNo")) {
            params.put("orderNo", params.get("orderNo"));
        }
        if (null != params.get("bankCardNo")) {
            params.put("bankCardNo", params.get("bankCardNo"));
        }
        if (null != params.get("userName")) {
            params.put("userName", params.get("userName"));
        }
        //同步状态
        dfStatusSync();
        //查询记录数据
        PageInfo<HcDfInfo> page = null;
        if (null != params.get("init")) {
            page = new PageInfo<>(null);
        } else {
            page = iCapitalFlowOrderService.getHcDfInfoPageList(params);
        }
        request.setAttribute("pageInfo", page);
        mav.setViewName("v2/capitalFlow/yiMaDaiPage");
        return mav;
    }


    /**
     * 代付提现
     *
     * @return
     */
    @RequestMapping(value = "/hcDf")
    @ResponseBody
    public AjaxResult hcDf(HttpServletRequest request, HttpServletResponse response) {
        BackUser user = loginAdminUser(request);
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String amount = request.getParameter("amount");
            String bankCardNo = request.getParameter("bankCardNo");
            String bankName = request.getParameter("bankName");
            String bankUserName = request.getParameter("bankUserName");
            if (StringUtils.isNotEmpty(amount)
                    && StringUtils.isNotEmpty(bankCardNo)
                    && StringUtils.isNotEmpty(bankName)
                    && StringUtils.isNotEmpty(bankUserName)
                    && NumberUtils.isNumber(amount)
            ) {
                //代付提现
                ApiServiceResult result = hcService.dfPayment(
                        user.getId().toString(),
                        bankUserName,
                        bankName,
                        bankCardNo,
                        new BigDecimal(amount),
                        "0"
                );
                if (result.isSuccessed()) {
                    //等待1秒
                    Thread.sleep(1000);
                    //同步状态
                    dfStatusSync();
                    //返回提示信息
                    ajaxResult.setMessage(result.getMsg() + "，若状态仍未同步请点击手动同步状态按钮进行状态同步");
                } else {
                    return new AjaxResult(Status.FAIL.getName(), result.getMsg());
                }
            } else {
                return new AjaxResult(Status.FAIL.getName(), "参数非法！");
            }
        } catch (Exception e) {
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        }

        return ajaxResult;
    }

    /**
     * 代付提现结果同步
     *
     * @return
     */
    @RequestMapping(value = "/hcDfSync")
    @ResponseBody
    public AjaxResult hcDfStatusSync(HttpServletRequest request, HttpServletResponse response) {
        BackUser user = loginAdminUser(request);
        AjaxResult ajaxResult = new AjaxResult();
        try {
            //同步状态
            dfStatusSync();
        } catch (Exception e) {
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        }
        return ajaxResult;
    }

    /**
     * 同步代付提现状态
     */
    private void dfStatusSync() {
        List<HcDfInfo> list = iCapitalFlowOrderService.getHcDfInfoList(null);
        if (CollectionUtils.isNotEmpty(list)) {
            for (HcDfInfo hcDfInfo : list) {
                if (hcDfInfo != null
                        && StringUtils.isNotEmpty(hcDfInfo.getStatus())
                        && StringUtils.isNotEmpty(hcDfInfo.getOrderNo())) {
                    try {
                        //同步状态
                        hcService.queryDfPayment(hcDfInfo.getOrderNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
