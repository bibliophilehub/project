package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.constants.SmsContentConstant;
import com.inext.dao.IBorrowUserDao;
import com.inext.entity.UserMessage;
import com.inext.exception.BaseException;
import com.inext.exception.CheckException;
import com.inext.exception.CheckSendNumException;
import com.inext.service.ISmsService;
import com.inext.service.IUserMessageService;
import com.inext.utils.RedisUtil;
import com.inext.utils.RequestUtils;
import com.inext.utils.StringUtils;
import com.inext.utils.SyjUtils;
import com.inext.utils.sms.SmsSendUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class SmsService implements ISmsService {
    Logger logger = LoggerFactory.getLogger(SmsService.class);
    @Resource
    IBorrowUserDao borrowUserDao;
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Autowired
    private IUserMessageService userMessageService;//消息中心

    @Override
    public void getSmsCode(HttpServletRequest request, JSONObject json) {
        try {
            smsCode(request, json);
        } catch (CheckException e) {
            logger.error(e.getMessage());
            setJsonMsg(json, e.getMessage(), "1");
        } catch (CheckSendNumException e) {
            logger.error(e.getMessage());
        } catch (BaseException e) {
            logger.error(e.getMessage());
            setJsonMsg(json, e.getMessage(), "-1");
        } catch (Exception e) {
            logger.error(e.getMessage());
            setJsonMsg(json, e.getMessage(), "-1");
        }

    }

    private void smsCode(HttpServletRequest request, JSONObject json) throws BaseException, CheckSendNumException, CheckException {
        //校验信息
        checkSendSms(request, json);
        //用户手机号码
        String userPhone = request.getParameter("userPhone");
        logger.debug("接受短信验证码用户：" + userPhone);
        String rand = String.valueOf(Math.random()).substring(2).substring(0, 4);// 4位固定长度
        String content = SmsContentConstant.getLoginCode(String.valueOf(rand));
        //将短信进行发送
        // 2018-08-23 10:59:00 短信验证码供应商由创蓝更换至天畅
        //SmsSendUtil.sendByTianChang(userPhone, content);
        String result = SmsSendUtil.sendSmsDiyCLandYX(userPhone, content);
        if (result != null && "0".equals(result)) {
            //将验证码缓存到redis
            setRedisInfo(userPhone, rand);
            saveSmsMessage(request, userPhone, content, rand);
            json.put("code", "0");
            json.put("message", "短信验证码已发送,请注意查收~");
            JSONObject js = new JSONObject();
            js.put("code", "0");
            js.put("message", "短信验证码已发送,请注意查收~");
            json.put("result", js);
        } else {
            json.put("code", "-1");
            json.put("message", "短信验证码发送失败");
            JSONObject js = new JSONObject();
            js.put("code", "-1");
            js.put("message", "短信验证码发送失败");
            json.put("result", js);
        }
        //財焱短信
//        boolean caiYan = SmsSendUtil.sendSMSByCaiYan(userPhone, content);
//        if(!caiYan)
//        {
//            //将验证码缓存到redis
//            setRedisInfo(userPhone, rand);
//            saveSmsMessage(request, userPhone, content, rand);
//            json.put("code", "0");
//            json.put("message", "短信验证码已发送,请注意查收~");
//            JSONObject js = new JSONObject();
//            js.put("code", "0");
//            js.put("message", "短信验证码已发送,请注意查收~");
//            json.put("result", js);
//        }else {
//            json.put("code", "-1");
//            json.put("message", "短信验证码发送失败");
//            JSONObject js = new JSONObject();
//            js.put("code", "-1");
//            js.put("message", "短信验证码发送失败");
//            json.put("result", js);
//
//        }

    }


    private void saveSmsMessage(HttpServletRequest request, String userPhone, String content, String rand) {
        UserMessage message = new UserMessage();
        message.setMessageAddress(userPhone);//手机号
        message.setMessageSendDatetime(new Date());
        if (StringUtils.isBlank(content)) {
            message.setMessageContent(rand);
        } else {
            message.setMessageContent(content);
        }
        message.setMessageSendIp(RequestUtils.getIpAddr(request));
        message.setMessageTitle("短信验证码");
        message.setMessageStatus(UserMessage.STATUS_UNREAD);//发送状态 0：未读
        userMessageService.saveUserMsg(message);//添加短信记录
    }

    private void setJsonMsg(JSONObject json, String msg, String code) {
        json.put("code", "-1");
        json.put("message", msg);
        JSONObject js = new JSONObject();
        js.put("code", code);
        js.put("message", msg);
        json.put("result", js);
    }

    private void setRedisInfo(String userPhone, String rand) {
        String key = RedisCacheConstants.SMS_CODE_VERIFICATION_LOGIN_PREFIX + userPhone;
        redisUtil.set(key, rand, Constants.KEYTIME);
    }

    private void checkSendSms(HttpServletRequest request, JSONObject json) throws BaseException, CheckSendNumException, CheckException {
        //验证手机号码是否为空或者是否正确或者参数是否正确
        checkUserPhone(request, json);
        String ip = RequestUtils.getIpAddr(request);
        //验证ip是否是白名单或者验证手机号码是否是白名单
        checkSafetyCode(request, json);
    }

    private void checkSafetyCode(HttpServletRequest request, JSONObject json) throws CheckSendNumException {
        String userPhone = request.getParameter("userPhone");
        String ip = RequestUtils.getIpAddr(request);
        //返回前端错误码
        String code = "-1";
        userPhone = userPhone.trim();
        //判断一分钟内是否获取验证码过于频繁
        checkUserOne(userPhone, json, code);
    }

    private void checkUserOne(String userPhone, JSONObject json, String code) throws CheckSendNumException {
        String redisKey = RedisCacheConstants.SMS_CODE_IS_PROTECT_LIMIT_PREFIX + userPhone;
        if (redisUtil.get(redisKey) == null) {
            //设置有效期一分钟
            redisUtil.set(redisKey, 1, new Long(60));
        } else {
            logger.info("操作有点频繁，请一分钟之后再尝试！");
            String msg = "操作有点频繁，请一分钟之后再尝试！";
            code = "-1";
            setJsonMsg(json, msg, code);
            json.put("code", code);
            throw new CheckSendNumException("请一分钟后再尝试发送!");
        }
    }

    private void checkUserPhone(HttpServletRequest request, JSONObject json) throws BaseException {
        //用户手机号码
        String userPhone = request.getParameter("userPhone");
        if (userPhone == null || "".equals(userPhone)) {
            throw new BaseException("手机号码为空！");
        } else {
            if (!SyjUtils.isChinaPhoneLegal(userPhone)) {
                throw new BaseException("手机号码 不合法！");
            }
        }
    }
}
