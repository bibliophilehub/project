package com.inext.controller;


import com.inext.constants.RedisCacheConstants;
import com.inext.entity.BorrowUser;
import com.inext.enumerate.Status;
import com.inext.result.ResponseDto;
import com.inext.result.ServiceResult;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.utils.JSONUtil;
import com.inext.utils.MD5Utils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户借款记录
 *
 * @param
 * @author
 */
@Controller
@RequestMapping("credit-loan/")
public class UserloanController extends BaseController {
    Logger logger = Logger.getLogger(UserloanController.class);
    @Autowired
    private IAssetBorrowOrderService iAssetBorrowOrderService;
    @Autowired
    private IBorrowUserService iBorrowUserService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;

    /**
     * 申请借款
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "申请借款(我要卖手机)", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "perPayMoney", value = "预付款金额", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "penaltyAmount", value = "违约金额", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "moneyDay", value = "履约期限", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "moneyAmount", value = "应退还总额", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPassword", value = "支付密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceNumber", value = "设备号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceModel", value = "手机型号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型(android,ios,h5) 本机参数", dataType = "String", paramType = "query")
    })
    @RequestMapping("apply-loan")
    public void applyLoan(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String key = "qhg_borrow_apply_loan";
        String code = Status.FAIL.getName();
        String msg = "申请失败";

        String results = "";
        boolean isDelKey = true;
        try {
            Map<String, String> params = getParameters(request);

//            Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
//            String perPayMoney=loan.get("LOAN_PER");//预付款
//            String penaltyAmount=loan.get("LOAN_WY");//违约金
//            String moneyAmount=loan.get("LOAN_ALL");//应退还总额
//            String moneyDay=loan.get("LOAN_DAY");
//            params.put("perPayMoney",perPayMoney);
//            params.put("penaltyAmount",penaltyAmount);
//            params.put("moneyAmount",moneyAmount);
//            params.put("moneyDay",moneyDay);

                    BorrowUser bu = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));

            if (bu == null) {
                msg = "请先登录";
                return;
            }
            params.put("userId",bu.getId()+"");
            ServiceResult serviceResults=allowBorrow((HashMap<String, String>) params);
            if(!serviceResults.getCode().equals(ServiceResult.SUCCESS)){
                msg= serviceResults.getMsg();
                return;
            }

            bu = iBorrowUserService.getBorrowUserById(bu.getId());

            key = key + "_" + bu.getId(); // 确定key唯一

            if (null != redisUtil.get(key)) {
                isDelKey = false;
                msg = "您的借款申请正在处理中，请稍后再试";
            } else {
                redisUtil.set(key, "1", (long) 60);

                params.put("ip",getIpAddr(request));
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("userId", bu.getId() + "");
                map2.put("perPayMoney", params.get("perPayMoney"));
                map2.put("moneyDay", params.get("moneyDay"));
                map2.put("deviceNumber", params.get("deviceNumber"));
                ServiceResult serviceResult = allowBorrow(map2);
                if (serviceResult.isSuccessed()) {
                    String checkPassWord = MD5Utils.MD5Encode(params.get("payPassword"));

                    if (!bu.getTransactionPassword().equals(checkPassWord)) {
                        msg = "支付密码错误";
                        return;
                    }

                    Map<String, Object> map = iAssetBorrowOrderService.saveLoan(params, bu);
                    code = map.get("code").toString();
                    msg = map.get("msg").toString();
                    results=map.get("result").toString();
                    if (code == Status.SUCCESS.getName()) {
                        redisUtil.remove(key);
                    }
                } else {
                    msg = serviceResult.getMsg();
                }
            }

        } catch (Exception e) {
            logger.error("credit-loan_get-apply-loan======", e);
            e.printStackTrace();
            code = Status.ERROR.getName();
            msg = Status.ERROR.getValue();
        } finally {
            if (isDelKey) {
                try {
                    redisUtil.remove(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            result.put("code", code);
            result.put("message", msg);
            result.put("result", results);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
        }
    }

    public ServiceResult allowBorrow(HashMap<String, String> params) {
        ServiceResult serviceResult = new ServiceResult("500", "未知异常");
        Integer userId = Integer.valueOf(params.get("userId"));
        String deviceNumber = params.get("deviceNumber");
        try {
            BorrowUser user = iBorrowUserService.getBorrowUserById(userId);
            Date date = new Date();

            if (StringUtils.isBlank(params.get("perPayMoney")) || StringUtils.isBlank(params.get("moneyDay"))) {
                serviceResult.setMsg("参数错误");
            } else {

                    if (1 != user.getIsVerified().intValue()) {
                        serviceResult.setMsg("请先实名认证");
                    } else {
                        if (1 == user.getIsBlack().intValue()) {
                            serviceResult.setMsg("您为黑名单用户，暂不能借款");
                        } else {
                            if (1 != user.getIsOperator().intValue()) {
                                serviceResult.setMsg("手机运营商未认证");
                            } else {
                            	
//                            	if(1 !=user.getIsZhima().intValue()){
//                            		serviceResult.setMsg("芝麻信用未授权或者授权已过期(请检查您的APP是否为最新版本，并至最新版本完善芝麻授信)");
//                            	}else{
                            		
                            		if (1 != user.getIsPhone().intValue()) {
                            			serviceResult.setMsg("未提交通讯录");
                            		} else {
                            			if (1 != user.getIsCard().intValue()) {
                            				serviceResult.setMsg("未绑定银行卡");
                            			} else {
                            				Integer checkResult = iAssetBorrowOrderService.checkBorrow(userId, null);
                            				if (1 == checkResult) {
                            					serviceResult.setMsg("您有借款申请正在审核或未还款完成，暂不能借款。");
                            				} else {
                            					Map<String, String> interval = iAssetBorrowOrderService
                            							.findAuditFailureOrderByUserId(user.getId());
                            					if ("-1".equals(interval.get("code"))) {
                            						serviceResult.setExt(interval.get("canLoan"));
                            						serviceResult.setMsg(interval.get("msg"));
                            					} else {
                            						serviceResult = new ServiceResult(ServiceResult.SUCCESS, "可以借款");
                            					}
                            				}
                            			}
                            		}
//                            	}
                            }
                        }
                    }
                }
        } catch (Exception e) {
            logger.error("allowBorrow error ", e);
        } finally {
            return serviceResult;
        }
    }
    
    


}
