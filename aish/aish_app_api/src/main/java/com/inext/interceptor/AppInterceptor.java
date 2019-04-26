package com.inext.interceptor;

import com.google.common.collect.Maps;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.AppAutoLoginLog;
import com.inext.entity.BorrowUser;
import com.inext.service.IAppAutoLoginLogService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IUserLoginRecordService;
import com.inext.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by 李思鸽 on 2017/3/22 0022.
 * APP请求拦截处理
 */


public class AppInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(AppInterceptor.class);

    IAppAutoLoginLogService appLogService;//登录记录

    IBorrowUserService borrowUserService;

    RedisUtil redisUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        System.out.println(">>>>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
        if (vaildToken(request, response)) return false;
        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    /**
     * 验证token是否有效
     *
     * @param request
     * @param response
     * @return
     */
    private boolean vaildToken(HttpServletRequest request, HttpServletResponse response) {
        borrowUserService = SpringContextHolder.getBean("borrowUserService");
        appLogService = SpringContextHolder.getBean("appAutoLoginLogService");
        redisUtil = SpringContextHolder.getBean("redisUtil");

        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            response.setStatus(401);
            return true;
        }

        Map<String, String> params = Maps.newHashMap();
        params.put("token", token);
        params.put("date", DateUtils.getDate(
                DateUtils.settingDate(DateUtils.getYear(), Integer.parseInt(DateUtils.getMonth()), Integer.parseInt(DateUtils.getDay()) - 3)));
        AppAutoLoginLog appAutologinLog = appLogService.selectByParams(params);
//         如果等于NULL 那么说明该token已失效
        if (appAutologinLog == null) {
            response.setStatus(401);
            return true;
        }
        // 当前token有效，更新有效时间
        appAutologinLog.setLoginTime(new Date());
        appAutologinLog.setEffTime(TokenUtils.getEffTime(new Date()));
        this.appLogService.updateAPPAutologinLog(appAutologinLog);

        BorrowUser bu = borrowUserService.getBorrowUserById(appAutologinLog.getUserId());
        String tokenJson = TokenUtils.generateToken(bu);
        String key = RedisCacheConstants.TOKENCODE + bu.getUserAccount();
        redisUtil.set(key, tokenJson, RedisCacheConstants.TOKENTIME);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        System.out.println(">>>>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        System.out.println(">>>>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}
