package com.inext.service.impl;

import com.alibaba.fastjson.JSON;
import com.inext.entity.BorrowUser;
import com.inext.entity.UserMessage;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBaoFooPayService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IUserMessageService;
import com.inext.service.rmi.webservice.YeePayService;
import com.inext.utils.RedisUtil;
import com.inext.utils.RequestUtils;
import com.inext.utils.StringUtils;
import com.inext.utils.baofoo.XinyanAuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("baoFooPayService")
public class BaoFooPayServiceImpl implements IBaoFooPayService {
    private static Logger logger = LoggerFactory.getLogger(BaoFooPayServiceImpl.class);
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Resource
    IBorrowUserService borrowUserService;

    @Autowired
    XinyanAuthUtil xinyanAuthUtil;

    @Autowired
    YeePayService yeePayService;

    @Autowired
    private IUserMessageService userMessageService;//消息中心


    @Override
    public Map<String, Object> getBindCardToken(Map<String, String> tokenParaMap) {
        String userId = tokenParaMap.get("userId");
        String mobilePhone = tokenParaMap.get("phone"); //预留手机号
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            Map<String, String> yopResult = yeePayService.userBindCard(tokenParaMap);

            logger.info("tokenValidateS userId=" + userId + " result:" + yopResult);
            if (yopResult == null || yopResult.isEmpty()) {
                logger.info("易宝绑卡返回为空，发生异常");
                retMap.put("code", "9999");
                retMap.put("msg", "服务器开小车了，请稍候再试");
                return retMap;
            }
            logger.info("易宝绑卡返回值：{}", JSON.toJSONString(yopResult));

            if ("0".equals(yopResult.get("code"))) {
                String retMsg = "短信验证码发送成功";
                String requestno = yopResult.get("requestno");
                String smscode = yopResult.get("smscode");
                StringBuffer sb = new StringBuffer("易宝绑卡短信发送:");
                sb.append(smscode);
                // 设置redis
                redisUtil.set("BINDCARD_SENDMSG_USERID" + userId, requestno, 1500L);
                saveSmsMessage(mobilePhone, sb.toString(), requestno);
                retMap.put("code", "0000");
                retMap.put("msg", retMsg);
                return retMap;
            } else {
                String retMsg = "短信验证码发送失败";
                logger.error("用户易宝绑卡发送短信失败，用户手机号：{},用户id为:{},错误原因：{}", mobilePhone, userId, yopResult.get("msg"));
                retMap.put("code", "9999");
                retMap.put("msg", retMsg);
                return retMap;
            }
        } catch (Exception e) {
            logger.info("getYOP error userId=" + userId, e);
            retMap.put("code", "9999");
            retMap.put("msg", "系统繁忙 请稍候再试");
            return retMap;
        }
    }

    private void saveSmsMessage(String userPhone, String content, String rand) {
        UserMessage message = new UserMessage();
        message.setMessageAddress(userPhone);//手机号
        message.setMessageSendDatetime(new Date());
        if (StringUtils.isBlank(content)) {
            message.setMessageContent(rand);
        } else {
            message.setMessageContent(content);
        }
        message.setMessageSendIp(RequestUtils.getIpAddr());
        message.setMessageTitle("短信验证码");
        message.setMessageStatus(UserMessage.STATUS_UNREAD);//发送状态 0：未读
        userMessageService.saveUserMsg(message);//添加短信记录
    }

    @Override
    public ApiServiceResult bindCardValidate(Map<String, String> validateParaMap) throws IOException {

        String userId = validateParaMap.get("userId");
        try {
            Map<String, String> yopResult = yeePayService.userBindCardConfrim(validateParaMap);

            logger.info("tokenValidateS userId=" + userId + " result:" + yopResult);
            if (yopResult == null || yopResult.isEmpty()) {
                return new ApiServiceResult(ApiStatus.FAIL.getCode(), "绑卡功能错误，请联系客服");
            }
            logger.info("易宝绑卡返回值：{}", JSON.toJSONString(yopResult));

            if ("0".equals(yopResult.get("code"))) {
                BorrowUser bu = new BorrowUser();
                bu.setId(Integer.parseInt(userId));
                bu.setCardCode(validateParaMap.get("cardNo"));
                bu.setCardType(new Integer(validateParaMap.get("bankId")));
                bu.setCardPhone(validateParaMap.get("phone"));
                bu.setIsCard(1);
                bu.setIsYop(1);
                borrowUserService.updateUser(bu);
                return new ApiServiceResult("绑卡成功");
            } else {
                String errorcode = yopResult.get("errorcode");
                String message = "绑卡失败，请稍后再试......";
                if ("DK0500004".equals(errorcode)) {
                    message = "同人绑卡达到上限";
                } else if ("DK1001044".equals(errorcode)) {
                    message = "绑卡受限，请联系客服人员";
                } else if ("DK05000010".equals(errorcode)) {
                    message = "绑卡受限，绑卡已经超过3次";
                } else if ("DK2010050".equals(errorcode)) {
                    message = "一分钟内同一手机号校验过频繁，请稍后再试";
                } else if ("DK2010056".equals(errorcode)) {
                    message = "预留手机号与开户行归属地不一致";
                } else if ("DK8010031".equals(errorcode)) {
                    message = "银行卡信息或银行预留手机号有误";
                } else if ("DK8010033".equals(errorcode)) {
                    message = "未开通电子支付或身份证号、姓名、手机号有误，请联系银行或换卡支付";
                }
                return new ApiServiceResult(ApiStatus.FAIL.getCode(), message);
            }
        } catch (Exception e) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "绑卡功能错误，请联系客服");
        }
    }
}
