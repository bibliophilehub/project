package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IBankAllInfoDao;
import com.inext.entity.*;
import com.inext.enumerate.Status;
import com.inext.enums.RepaymentLockMsgEnum;
import com.inext.result.AjaxResult;
import com.inext.service.*;
import com.inext.utils.DateUtil;
import com.inext.utils.ExcelUtil;
import com.inext.utils.RedisUtil;
import com.inext.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wenkk on 2018/3/21 09：31. 还款订单
 */
@Controller
@RequestMapping("assetRepaymentOrder")
public class AssetRepaymentOrderController extends BaseController {
    Logger logger = LoggerFactory.getLogger(AssetRepaymentOrderController.class);

    @Resource
    IAssetRepaymentOrderService iAssetRepaymentOrderService;
    @Resource
    private IChannelService channelService;
    @Autowired
    IAssetBorrowOrderService iAssetBorrowOrderService;
    @Autowired
    IAssetRenewalOrderService iAssetRenewalOrderService;
    @Autowired
    IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Autowired
    RedisUtil redisUtil;
    @Resource
    private IBankAllInfoDao bankAllInfoDao;

    /**
     * 待还款,已还款,已逾期列表查询
     */
    @RequestMapping("pageList")
    public ModelAndView getPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        PageInfo<AssetRepaymentOrder> page = null;
        if (null == params.get("init")) {
            if ("8".equals(params.get("type")) || "9".equals(params.get("type")))
                page = iAssetRepaymentOrderService.getListForRepaymented(params);
            else
                page = iAssetRepaymentOrderService.getPageList(params);
        } else {
            page = getPage();
        }
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("orderStatus", params.get("orderStatus"));
        //已还款列表查询以实际放款时间算
        if ("8".equals(params.get("type")) || "9".equals(params.get("type"))) {
            if (null == params.get("startRepayDate") || null == params.get("endRepayDate") || "".equals(params.get("startRepayDate")) || "".equals(params.get("endRepayDate"))) {
                Map<Integer, String> dateMap = null;
                dateMap = getInitQueryStartDate(true);
                params.put("startRepayDate", dateMap.get(1));
                params.put("endRepayDate", dateMap.get(2));
            }
        } else {
            if (null == params.get("startDate") || null == params.get("endDate") || "".equals(params.get("startDate")) || "".equals(params.get("endDate"))) {
                Map<Integer, String> dateMap = null;
                if ("6".equals(params.get("type")))
                    dateMap = getInitQueryStartDate(false);//待还款查询开始日期为当天
                else
                    dateMap = getInitQueryStartDate(true);
                params.put("startDate", dateMap.get(1));
                params.put("endDate", dateMap.get(2));
            }
        }
        List<BankAllInfo> bankInfo = bankAllInfoDao.findBankAllInfo();
        request.setAttribute("bankAllInfo", bankInfo);
        mav.addObject("penaltyAmount", params.get("penaltyAmount"));
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("startRepayDate", params.get("startRepayDate"));
        mav.addObject("endRepayDate", params.get("endRepayDate"));
        mav.addObject("channelId", params.get("channelId"));
        mav.addObject("repaymentType", params.get("repaymentType"));
        mav.addObject("paymentChannel", params.get("paymentChannel"));
        mav.addObject("orderno", params.get("orderno"));//还款单号查询,已还款页面
        mav.addObject("sortColumn", params.get("sortColumn"));//排序
        mav.addObject("lateLevel", params.get("lateLevel"));//逾期手别查询
        mav.addObject("renewalRepaymentTime", params.get("renewalRepaymentTime"));

        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        mav.addObject("channelList", channelList);
        mav.addObject("paymentChannels", PaymentChannel.DESCRIPTION);


        if ("6".equals(params.get("type"))) {
            mav.addObject("orderStatusMap", AssetRepaymentOrder.orderStatusMap);
            mav.setViewName("v2/assetRepaymentOrder/assetRepaymentOrderList");
        } else if ("7".equals(params.get("type"))) {
            mav.setViewName("v2/assetRepaymentOrder/lateOrderList");
        } else if ("8".equals(params.get("type"))) {
            mav.addObject("repaymentTypeMap", AssetRepaymentOrder.repaymentTypeMap);
            mav.setViewName("v2/assetRepaymentOrder/assetRepaymentedOrderList");
        } else if ("9".equals(params.get("type"))) {
            mav.addObject("repaymentTypeMap", AssetRepaymentOrder.repaymentTypeMap);
            mav.setViewName("v2/collection/assetRepaymentedOrderList");
        }
        return mav;
    }

    /**
     * 非管理员查询待还款列表
     * 客服使用
     * 2019-01-23 16:21:43
     *
     * @param request
     * @return
     */
    @RequestMapping("queryPageList")
    public ModelAndView getQueryPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        PageInfo<AssetRepaymentOrder> page = null;
        params.put("type", "6");
        if (null == params.get("init"))
            page = iAssetRepaymentOrderService.getPageList(params);
        else
            page = getPage();
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("orderStatus", params.get("orderStatus"));
        if (null == params.get("startRepayDate") || null == params.get("endRepayDate") || "".equals(params.get("startRepayDate")) || "".equals(params.get("endRepayDate"))) {
            Map<Integer, String> dateMap = getInitQueryStartDate(false);
            params.put("startRepayDate", dateMap.get(1));
            params.put("endRepayDate", dateMap.get(2));
        }
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.addObject("startRepayDate", params.get("startRepayDate"));
        mav.addObject("endRepayDate", params.get("endRepayDate"));
        mav.addObject("channelId", params.get("channelId"));
        mav.addObject("repaymentType", params.get("repaymentType"));
        mav.addObject("paymentChannel", params.get("paymentChannel"));
        mav.addObject("sortColumn", params.get("sortColumn"));//排序

        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        mav.addObject("channelList", channelList);
        mav.addObject("paymentChannels", PaymentChannel.DESCRIPTION);

        mav.addObject("orderStatusMap", AssetRepaymentOrder.orderStatusMap);
        mav.setViewName("v2/assetRepaymentOrder/queryAssetRepaymentOrderList");

        return mav;
    }

    /**
     * 待还款 已逾期 导出
     */
    @RequestMapping("pageListExport")
    public void pageListExport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iAssetRepaymentOrderService.getList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("order_id", "订单号");
            title.put("user_name", "姓名");
            title.put("user_phone", "手机号");
            title.put("money_amount", "借款金额");
            title.put("penalty_amount", "违约金");
            title.put("plan_late_fee", "滞纳金");
            title.put("repayment_amount", "累计应还金额");
            title.put("repaymented_amount", "已还金额");
            title.put("credit_repayment_time", "放款时间");
            title.put("repayment_time", "应还款时间");
            title.put("late_day", "逾期天数");
            title.put("orderStatus", "状态");
            title.put("score", "风控分");
            title.put("detail", "风控消息");
            title.put("channel_name", "渠道");
            title.put("isOld", "老用户");
            ExcelUtil.writableWorkbook(out, list, title);
            out.flush();
            String type = params.get("type").toString();
            int module = 0;
            String log = "";
            if ("6".equals(type)) {
                module = 192;
                log = "待还款列表导出";
            } else if ("7".equals(type)) {
                module = 199;
                log = "已逾期期列表导出";
            }
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
     * 已还款列表导出
     */
    @RequestMapping("repaymentedpageListExport")
    public void repaymentedPageList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iAssetRepaymentOrderService.getList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("order_id", "订单号");
            title.put("user_name", "姓名");
            title.put("user_phone", "手机号");
            title.put("money_amount", "借款金额");
            title.put("penalty_amount", "违约金");
            title.put("plan_late_fee", "滞纳金");
            title.put("repayment_amount", "累计应还金额");
            title.put("cedit_amount", "累计减免金额");
            title.put("repaymented_amount", "累计已还金额");
            title.put("credit_repayment_time", "放款时间");
            title.put("paymentChannelName", "资方");
            title.put("repayment_time", "应还款时间");
            title.put("orderno", "还款单号");
            title.put("repayment_real_time", "还款时间");
            title.put("repaymentType", "还款方式");
            title.put("repaymentChannel", "还款渠道");
            title.put("late_day", "逾期天数");
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
     * 管理员还款
     *
     * @param request
     * @param response
     * @return
     */
//    public AjaxResult trueRepaymentMoney(HttpServletRequest request, HttpServletResponse response, AssetRepaymentDetail assetRepaymentDetail) {
//        BackUser operbackUser = (BackUser) RequestUtils.getRequest().getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
//        assetRepaymentDetail.setAdminUsername(operbackUser.getAccount());
//        iAssetRepaymentOrderService.trueRepaymentMoney(assetRepaymentDetail);
//        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
//    }
    @RequestMapping(value = "/trueRepaymentMoney")
    @ResponseBody
    public AjaxResult trueRepaymentMoney(HttpServletRequest request, HttpServletResponse response, AssetRepaymentDetail assetRepaymentDetail) {

        //添加Detail还款方式为后台还款
        assetRepaymentDetail.setRepaymentChannel(1);
        assetRepaymentDetail.setType(1);
        if (assetRepaymentDetail.getRepaymentOrderNo() == null || "".equals(assetRepaymentDetail.getRepaymentOrderNo()))
            return new AjaxResult(Status.ERROR.getName(), "缺少还款单号");

        //添加还款单号唯一性校验
        long count = iAssetRepaymentOrderService.getOrderByRepaymentOrderNo(assetRepaymentDetail.getRepaymentOrderNo());
        if (count > 0) {
            return new AjaxResult(Status.ERROR.getName(), "还款单号重复，请核对后进行填写");
        }
        if (assetRepaymentDetail.getOrderId() == null) {
            return new AjaxResult(Status.ERROR.getName(), "缺少orderId");
        }

        if (assetRepaymentDetail.getTrueRepaymentMoney() == null) {
            return new AjaxResult(Status.ERROR.getName(), "缺少trueRepaymentMoney");
        }

        AssetBorrowOrder abo = iAssetBorrowOrderService.getOrderById(assetRepaymentDetail.getOrderId());

        if (abo == null) {
            return new AjaxResult(Status.ERROR.getName(), "不存在订单");
        }
        List<Integer> statuses = Arrays.asList(//
                AssetOrderStatusHis.STATUS_YHK, //已还款
                AssetOrderStatusHis.STATUS_JCHG//检测合格
        );
        if (statuses.contains(abo.getStatus())) {
            return new AjaxResult(Status.ERROR.getName(), "该订单状态为[" + AssetOrderStatusHis.orderStatusMap.get(abo.getStatus()) + "] 不能执行该操作");
        }

        BackUser operbackUser = (BackUser) RequestUtils.getRequest().getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        assetRepaymentDetail.setAdminUsername(operbackUser.getAccount());


        AssetRepaymentOrder aroModel = iAssetRepaymentOrderService.getRepaymentByOrderId(assetRepaymentDetail.getOrderId());

        //当前减免金额  +  当前还款金额
        BigDecimal sumCurrentAmount = (assetRepaymentDetail.getCeditAmount() == null ? new BigDecimal(0) : assetRepaymentDetail.getCeditAmount()).add(assetRepaymentDetail.getTrueRepaymentMoney());

        //剩余应还
        BigDecimal shengyuAmount = aroModel.getRepaymentAmount().subtract(aroModel.getRepaymentedAmount()).subtract(aroModel.getCeditAmount() == null ? new BigDecimal(0) : aroModel.getCeditAmount());

        logger.info("当前减免金额  +  当前还款金额 :" + sumCurrentAmount);

        logger.info("shengyuAmount:" + shengyuAmount);

        if (sumCurrentAmount.compareTo(shengyuAmount) > 0) {
            return new AjaxResult(Status.ERROR.getName(), "减免金额加还款金额不能大于剩余还款金额");
        }

        //当前续期订单是否处于支付中
        final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + aroModel.getId();
        final String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
        //拿锁 能拿到锁 则可以进行下面操作
        if (!redisUtil.lock(lockKey, time)) {
            String lockKeyMsg = lockKey + RedisUtil.ORDER_PAY_LOCK_MSG;// 锁消息提示
            //获取 是谁加的锁
            Object objectLock = redisUtil.get(lockKeyMsg);
            logger.info("当前订单已经在进行续期=" + lockKey);
            if (objectLock != null) {
                int value = Integer.valueOf(objectLock.toString()).intValue();
                return new AjaxResult(Status.ERROR.getName(), RepaymentLockMsgEnum.getDescByValue(value) + ",不能执行该操作");
            } else {
                return new AjaxResult(Status.ERROR.getName(), "该订单正在处理中,不能执行该操作");
            }
        }


        try {
            iAssetRepaymentOrderService.trueRepaymentMoney(aroModel, assetRepaymentDetail);
        } catch (Exception e) {
            logger.info("后台还款异常:", e.getMessage());
            return new AjaxResult(Status.FAIL.getName(), Status.FAIL.getValue());
        } finally {
            redisUtil.unlock(lockKey);
        }


        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }

    private String getExlName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date()) + System.currentTimeMillis() + ".xls";
    }

    /**
     * 管理员续期
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/trueRenewalMoney")
    @ResponseBody
    public AjaxResult trueRenewalMoney(HttpServletRequest request, HttpServletResponse response, AssetRenewalOrder assetRenewalOrder) {
        BackUser user = loginAdminUser(request);
        if (assetRenewalOrder.getOrderId() == null) {
            return new AjaxResult(Status.ERROR.getName(), "缺少订单号");
        }
        if (assetRenewalOrder.getRenewalType() == null) {
            return new AjaxResult(Status.ERROR.getName(), "缺少续期类型");
        }
        if (assetRenewalOrder.getRenealComment() == null) {
            return new AjaxResult(Status.ERROR.getName(), "备注不能为空");
        }
        //添加还款单号唯一性校验
        long count = iAssetRepaymentOrderService.getOrderByRepaymentOrderNo(assetRenewalOrder.getRenewalOrderNo());
        if (count > 0) {
            return new AjaxResult(Status.ERROR.getName(), "电子回执单号重复，请核对后进行填写");
        }
        try {
            AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(assetRenewalOrder.getOrderId());
            AssetBorrowOrder assetBorrowOrder = iAssetBorrowOrderService.getOrderById(assetRenewalOrder.getOrderId());
            if (assetBorrowOrder.getStatus().equals(7)) {
                return new AjaxResult(Status.ERROR.getName(), "逾期用户不能续期");
            }
            Date repaymentTime = DateUtil.addDay(assetRepaymentOrder.getRepaymentTime(), assetBorrowOrder.getMoneyDay());
            //修改订单表最后到期时间
            AssetBorrowOrder order = new AssetBorrowOrder();
            order.setId(assetRenewalOrder.getOrderId());
            order.setLoanEndTime(repaymentTime);

            //为了逾期续期做准备
//            order.setStatus(3);
            iAssetBorrowOrderService.updateByPrimaryKeySelective(order);
            //修改还款表数据
            AssetRepaymentOrder assetRepaymentOrderNow = new AssetRepaymentOrder();
            assetRepaymentOrderNow.setId(assetRepaymentOrder.getId());
            assetRepaymentOrderNow.setOrderRenewal(9);
            assetRepaymentOrderNow.setUpdateTime(new Date());
            assetRepaymentOrderNow.setRepaymentTime(repaymentTime);
            //为了逾期续期做准备
//            assetRepaymentOrderNow.setOrderStatus(6);
//            assetRepaymentOrderNow.setLateDay(0);
//            assetRepaymentOrderNow.setPlanLateFee(new BigDecimal(0));
//            assetRepaymentOrderNow.setRepaymentAmount(assetRepaymentOrder.getRepaymentAmount().subtract(assetRepaymentOrder.getPlanLateFee()));

            iAssetRepaymentOrderService.update(assetRepaymentOrderNow);
            //添加续期记录
            assetRenewalOrder.setUserId(assetRepaymentOrder.getUserId());
            assetRenewalOrder.setUserPhone(assetRepaymentOrder.getUserPhone());
            assetRenewalOrder.setUserName(assetRepaymentOrder.getUserName());
            assetRenewalOrder.setMoneyAmount(assetRepaymentOrder.getMoneyAmount());
            assetRenewalOrder.setPenaltyAmount(assetRepaymentOrder.getPenaltyAmount());
            assetRenewalOrder.setMoneyDay(assetBorrowOrder.getMoneyDay());
            assetRenewalOrder.setCreditRepaymentTime(assetRepaymentOrder.getCreditRepaymentTime());
            assetRenewalOrder.setRenewalTime(new Date());
            assetRenewalOrder.setRenewalDay(assetBorrowOrder.getMoneyDay());
            assetRenewalOrder.setRepaymentTime(repaymentTime);
            int id = iAssetRenewalOrderService.insertRenewalOrder(assetRenewalOrder);

            //添加续期金额记录
            AssetRepaymentDetail assetRepaymentDetail = new AssetRepaymentDetail();
            assetRepaymentDetail.setOrderId(assetRenewalOrder.getOrderId());
            assetRepaymentDetail.setRenewalId(id);
            assetRepaymentDetail.setUserId(assetRepaymentOrder.getUserId());
            assetRepaymentDetail.setRepaymentChannel(3);
            assetRepaymentDetail.setRepaymentType(assetRenewalOrder.getRenewalType());
            assetRepaymentDetail.setType(2);
            assetRepaymentDetail.setBankName(assetRenewalOrder.getRenewalBank());
            assetRepaymentDetail.setRepaymentOrderNo(assetRenewalOrder.getRenewalOrderNo());
            assetRepaymentDetail.setTrueRepaymentMoney(assetRepaymentOrder.getPenaltyAmount());
            assetRepaymentDetail.setRemark(assetRenewalOrder.getRenealComment());
            assetRepaymentDetail.setCreatedAt(new Date());
            assetRepaymentDetail.setAdminUsername(user.getAccount());

            iAssetRepaymentOrderService.doSave(assetRepaymentDetail);
            //        添加订单历史记录
            AssetOrderStatusHis his = new AssetOrderStatusHis();
            his.setOrderId(order.getId());
            his.setOrderStatus(AssetOrderStatusHis.STATUS_YXQ);
            his.setRemark("已续期，\n" +
                    DateUtil.formatDate("yyyy年MM月dd日 HH:mm:ss", order.getLoanEndTime()) +
                    "前未还款，则视为逾期，平台将收取逾期利息。");
            his.setCreateTime(new Date());
            iAssetOrderStatusHisService.saveHis(his);
        } catch (Exception e) {
            return new AjaxResult(Status.ERROR.getName(), "后台处理异常请联系管理员");
        }
        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }

    /**
     * 管理员续期（手动）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/renewaloperate")
    @ResponseBody
    public AjaxResult renewaloperate(HttpServletRequest request, HttpServletResponse response, Integer orderId, String username, String userphone) {


        AssetBorrowOrder assetBorrowOrder = iAssetBorrowOrderService.getOrderById(orderId);

        if (assetBorrowOrder == null) {
            return new AjaxResult(Status.ERROR.getName(), "续期订单不存在");
        }

        if (!assetBorrowOrder.getUserName().equals(username) || !assetBorrowOrder.getUserPhone().equals(userphone)) {
            return new AjaxResult(Status.ERROR.getName(), "续期用户资料不匹配");
        }

        try {
            AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderService.getRepaymentByOrderId(assetBorrowOrder.getId());

            Date repaymentTime = DateUtil.addDay(assetRepaymentOrder.getRepaymentTime(), assetBorrowOrder.getMoneyDay());
            //修改订单表最后到期时间
            AssetBorrowOrder order = new AssetBorrowOrder();
            order.setId(assetBorrowOrder.getId());
            order.setLoanEndTime(repaymentTime);

            //为了逾期续期做准备
            order.setStatus(3);
            iAssetBorrowOrderService.updateByPrimaryKeySelective(order);
            //修改还款表数据
            AssetRepaymentOrder assetRepaymentOrderNow = new AssetRepaymentOrder();
            assetRepaymentOrderNow.setId(assetRepaymentOrder.getId());
            assetRepaymentOrderNow.setOrderRenewal(9);
            assetRepaymentOrderNow.setUpdateTime(new Date());
            assetRepaymentOrderNow.setRepaymentTime(repaymentTime);
            //为了逾期续期做准备
            assetRepaymentOrderNow.setOrderStatus(6);
            assetRepaymentOrderNow.setLateDay(0);
            assetRepaymentOrderNow.setPlanLateFee(new BigDecimal(0));
            assetRepaymentOrderNow.setRepaymentAmount(assetRepaymentOrder.getRepaymentAmount().subtract(assetRepaymentOrder.getPlanLateFee()));

            iAssetRepaymentOrderService.update(assetRepaymentOrderNow);
            //添加续期记录
            AssetRenewalOrder assetRenewalOrder = new AssetRenewalOrder();
            assetRenewalOrder.setOrderId(assetBorrowOrder.getId());
            assetRenewalOrder.setUserId(assetRepaymentOrder.getUserId());
            assetRenewalOrder.setUserPhone(assetRepaymentOrder.getUserPhone());
            assetRenewalOrder.setUserName(assetRepaymentOrder.getUserName());
            assetRenewalOrder.setMoneyAmount(assetRepaymentOrder.getMoneyAmount());
            assetRenewalOrder.setPenaltyAmount(assetRepaymentOrder.getPenaltyAmount());
            assetRenewalOrder.setMoneyDay(assetBorrowOrder.getMoneyDay());
            assetRenewalOrder.setCreditRepaymentTime(assetRepaymentOrder.getCreditRepaymentTime());
            assetRenewalOrder.setRenewalTime(new Date());
            assetRenewalOrder.setRenewalDay(assetBorrowOrder.getMoneyDay());
            assetRenewalOrder.setRepaymentTime(repaymentTime);
            assetRenewalOrder.setRenealComment("技术续期");
            iAssetRenewalOrderService.insertRenewalOrder(assetRenewalOrder);
            //        添加订单历史记录
            AssetOrderStatusHis his = new AssetOrderStatusHis();
            his.setOrderId(order.getId());
            his.setOrderStatus(AssetOrderStatusHis.STATUS_YXQ);
            his.setRemark("已续期，\n" +
                    DateUtil.formatDate("yyyy年MM月dd日 HH:mm:ss", order.getLoanEndTime()) +
                    "前未还款，则视为逾期，平台将收取逾期利息。");
            his.setCreateTime(new Date());
            iAssetOrderStatusHisService.saveHis(his);

            /**
             * 删除订单历史记录的逾期记录
             */
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", order.getId());
            params.put("orderStatus", AssetOrderStatusHis.STATUS_YYQ);
            iAssetOrderStatusHisService.removeHis(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(Status.ERROR.getName(), "后台处理异常请联系管理员");
        }
        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }


    @RequestMapping("/test")
    @ResponseBody
    public void gettest() {
        logger.info("=====调用推送逾期接口");
        iAssetRepaymentOrderService.pushCuiShouTask();
    }

    @RequestMapping("/testH")
    @ResponseBody
    public void gettestH() {
        logger.info("=====调用已还款接口");
        iAssetRepaymentOrderService.findRaymentedHuanKuan();
    }

    @RequestMapping("/testC")
    @ResponseBody
    public void gettestC() {
        logger.info("=====调用明天逾期查询接口");
        iAssetRepaymentOrderService.findCurrentOverdue();
    }

    @RequestMapping("/testD")
    @ResponseBody
    public void gettestD() {
        logger.info("=====调用明天逾期还款接口");
        iAssetRepaymentOrderService.pushCuishouNotOverdue();
    }

    @RequestMapping("/delUnderLineRe")
    @ResponseBody
    public String delUnderLineRe(@RequestParam("orderId") Integer orderId, @RequestParam("userPhone") String userPhone) {
        logger.info("=====delUnderLineRe order start=====");
        String result = "";
        try {
            result = iAssetRepaymentOrderService.delUnderLineRe(orderId, userPhone);
        } catch (Exception e) {
            logger.error("业务错误", e.getMessage(), e);
        }

        logger.info("=====delUnderLineRe order end=====");
        return result;
    }

}
