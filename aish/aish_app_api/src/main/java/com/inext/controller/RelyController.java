package com.inext.controller;

import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.BackConfigParams;
import com.inext.entity.RiskCreditUser;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IAssetOrderStatusHisService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IRiskCreditUserService;
import com.inext.utils.JSONUtil;
import com.inext.utils.StringUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("rely/")
public class RelyController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RelyController.class);
    @Resource
    IRiskCreditUserService iRiskCreditUserService;
    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    IAssetOrderStatusHisService iAssetOrderStatusHisService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;

    /**
     * 风控回调
     * @param request
     * @param response
     */
    @PostMapping("/fkCallback")
    public void fkCallback(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> params) {
        String serialIdStr = params.get("serialId");
        String riskIdStr = params.get("riskId");
        String scoreStr = params.get("score");
        String detail = params.get("detail");
        String mgs = "回调失败";
        Integer code = 400;
        logger.info("fkCallback",getParameters(request));

        try {
            if (StringUtils.isNotEmpty(serialIdStr) && StringUtils.isNotEmpty(riskIdStr) && StringUtils.isNotEmpty(scoreStr)) {
                Integer serialId = 0;
                serialId = Integer.parseInt(serialIdStr);
                RiskCreditUser user = new RiskCreditUser(serialId);
                user.setRiskId(riskIdStr);
                user.setScore(new BigDecimal(scoreStr));
                user.setFkStatus(2);
                if(StringUtils.isNotEmpty(detail))
                    user.setDetail(detail);
                iRiskCreditUserService.updateByPrimaryKeySelective(user);
                user = iRiskCreditUserService.getEntityById(serialId);
                AssetBorrowOrder order = new AssetBorrowOrder();
                if (user != null) {
                    AssetBorrowOrder temOrder=iAssetBorrowOrderService.getOrderById(user.getAssetId());
                    Map<String, String> loanMap = backConfigParamsService.getBackConfig(BackConfigParams.FK, null);
                    String fkRsScore = loanMap.get("FK_RS_SCORE");
logger.info("serialId:{},fkRsScore:{},user.getScore:{},进入人工审核:{}",serialId,fkRsScore,user.getScore(),StringUtils.isNotBlank(fkRsScore) && new BigDecimal("-1").compareTo(new BigDecimal(fkRsScore)) < 0
        && !(user.getScore().compareTo(new BigDecimal(fkRsScore)) < 0));
                    if(temOrder==null||temOrder.getStatus()==null||temOrder.getStatus()!=AssetOrderStatusHis.STATUS_DSH){
                        //如果订单不存在 或者状态已经不是待审核 在次处理
                    }else if (!(user.getScore().compareTo(new BigDecimal(loanMap.get("FK_SCORE"))) < 0)) {
//                    AssetBorrowOrder order = new AssetBorrowOrder();
                        order.setId(user.getAssetId());
                        order.setStatus(AssetOrderStatusHis.STATUS_DFK);
//                    iAssetBorrowOrderService.updateByPrimaryKeySelective(order);

                        AssetOrderStatusHis his = new AssetOrderStatusHis();
                        his.setOrderId(order.getId());
                        his.setOrderStatus(AssetOrderStatusHis.STATUS_DFK);
                        his.setCreateTime(new Date());
                        his.setRemark("认证资料信用审核已通过");
                        iAssetOrderStatusHisService.saveHis(his);
                        his.setOrderStatus(AssetOrderStatusHis.STATUS_DFK);
                        his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_DFK));
                        his.setCreateTime(new Date());

                        iAssetOrderStatusHisService.saveHis(his);
                    } else if(StringUtils.isNotBlank(fkRsScore) && new BigDecimal("-1").compareTo(new BigDecimal(fkRsScore)) < 0
                            && !(user.getScore().compareTo(new BigDecimal(fkRsScore)) < 0)) {
                        order.setId(user.getAssetId());
                        order.setStatus(AssetOrderStatusHis.STATUS_DRGSH);

                        AssetOrderStatusHis his = new AssetOrderStatusHis();
                        his.setOrderId(order.getId());
                        his.setOrderStatus(AssetOrderStatusHis.STATUS_DRGSH);
                        his.setRemark("等待人工审核");
                        his.setCreateTime(new Date());
                        iAssetOrderStatusHisService.saveHis(his);
                    } else {
//                    AssetBorrowOrder order = new AssetBorrowOrder();
                        order.setId(user.getAssetId());
                        order.setStatus(AssetOrderStatusHis.STATUS_SHJJ);
                        order.setOrderEnd(1);

                        AssetOrderStatusHis his = new AssetOrderStatusHis();
                        his.setOrderId(order.getId());
                        his.setOrderStatus(AssetOrderStatusHis.STATUS_SHJJ);
                        his.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_SHJJ));
                        his.setCreateTime(new Date());
                        iAssetOrderStatusHisService.saveHis(his);
                    }
                    order.setAuditTime(new Date());
                    iAssetBorrowOrderService.updateByPrimaryKeySelective(order);
                    mgs = "回调成功";
                    code = 200;
                }else {
                    mgs = "serialId 错误，serialId="+serialIdStr;
                }
            }else{
                mgs = "参数值校验未通过，params="+JSONObject.fromObject(params).toString();
            }
        } catch (Exception e) {

        }
        Map result = new HashMap();
        result.put("msg", mgs);
        result.put("code", code);
        JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
    }


    /**
     * 风控贷后回调
     * @param request
     * @param response
     */
    @GetMapping("/fkDhCallback")
    @ResponseBody
    public Map fkDhCallback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getParameters(request);
      return iAssetBorrowOrderService.fkDhCallback(params);
    }

    
    public static void main(String[] args) {
        if (!(new BigDecimal(102).compareTo(new BigDecimal(101)) < 0)) {
        	System.out.print(1);
        } else if(new BigDecimal("-1").compareTo(new BigDecimal(101)) < 0
                && !(new BigDecimal(102).compareTo(new BigDecimal(101)) < 0)) {
        	
            System.out.print(2);
        } else {
        	 System.out.print(3);
        }
	}
}
