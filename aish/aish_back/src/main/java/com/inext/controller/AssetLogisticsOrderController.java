package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.dao.IAssetBorrowOrderDao;
import com.inext.dao.IAssetLogisticsOrderDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetLogisticsOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.enumerate.AppStatus;
import com.inext.result.ResponseDto;
import com.inext.service.IAssetLogisticsOrderService;
import com.inext.utils.ExcelUtil;
import com.inext.view.result.AssetBorrowOrderStatusHistoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
 * Created by wenkk on 2018/3/27 09：31. 订单物流信息
 */
@Controller
@RequestMapping("assetLogisticsOrder")
public class AssetLogisticsOrderController extends BaseController {
    Logger logger = LoggerFactory.getLogger(AssetLogisticsOrderController.class);

    @Resource
    IAssetLogisticsOrderService iAssetLogisticsOrderService;
    @Resource
    IAssetLogisticsOrderDao iAssetLogisticsOrderDao;
    @Resource
    IAssetBorrowOrderDao iAssetBorrowOrderDao;

    /**
     * 订单物流信息列表查询
     */
    @RequestMapping("pageList")
    public ModelAndView getPageList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> params = this.getParametersO(request);
        PageInfo<AssetLogisticsOrder> page = iAssetLogisticsOrderService.getPageList(params);
        request.setAttribute("pageInfo", page);
        mav.addObject("orderId", params.get("orderId"));
        mav.addObject("userPhone", params.get("userPhone"));
        mav.addObject("userName", params.get("userName"));
        mav.addObject("auditStatus", params.get("auditStatus"));
        mav.addObject("startDate", params.get("startDate"));
        mav.addObject("endDate", params.get("endDate"));
        mav.setViewName("v2/assetLogisticsOrder/assetLogisticsOrderList");
        return mav;
    }

    /**
     * 邮寄审核
     *
     * @return
     */
    @PostMapping("audit")
    @ResponseBody
    public ResponseDto audit(Integer id, Integer auditStatus) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(AppStatus.FAIL.getCode());
        responseDto.setMessage(AppStatus.FAIL.getDesc());
        AssetLogisticsOrder assetLogisticsOrder = iAssetLogisticsOrderDao.selectByPrimaryKey(id);
        AssetBorrowOrder assetBorrowOrder = iAssetBorrowOrderDao.getOrderById(assetLogisticsOrder.getOrderId());
        if (AssetOrderStatusHis.STATUS_YYQ.intValue()!=assetBorrowOrder.getStatus().intValue()){
        	if(AssetOrderStatusHis.STATUS_YHK.intValue()==assetBorrowOrder.getStatus().intValue()){
        		responseDto.setMessage("订单已经还款!");
        	}else{
        		iAssetLogisticsOrderService.audit(id, auditStatus);
        		responseDto.setCode(AppStatus.SUCCESS.getCode());
        		responseDto.setMessage("审核成功!");
        	}
        }else {
            responseDto.setMessage("逾期订单不允许操作！");
        }

        return responseDto;
    }

    /**
     * 订单物流信息导出
     */
    @RequestMapping("pageListExport")
    public void pageListExport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel,charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + getExlName());
            List<Map<String, String>> list = iAssetLogisticsOrderService.getList(params);
            LinkedHashMap<String, String> title = new LinkedHashMap<>();
            title.put("order_id", "订单号");
            title.put("user_name", "姓名");
            title.put("user_phone", "手机号");
            title.put("money_amount", "借款金额");
            title.put("money_day", "借款期限");
            title.put("penalty_amount", "违约金");
            title.put("device_model", "手机型号");
            title.put("device_number", "设备号");
            title.put("ogistics_no", "快递单号");
            title.put("ogistics_name", "快递公司");
            title.put("create_time", "邮寄时间");
            title.put("auditStatus", "质检状态");
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
