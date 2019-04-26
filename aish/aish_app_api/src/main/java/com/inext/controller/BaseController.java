package com.inext.controller;

import com.inext.dao.IBorrowUserDao;
import com.inext.entity.BorrowUser;
import com.inext.utils.IpUtil;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.TokenUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ttj
 */
public class BaseController {

    public static final String MESSAGE = "message";
    private static Logger loger = Logger.getLogger(BaseController.class);
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Autowired
    private IBorrowUserDao userDao;

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, String> ReadCookieMap(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        return IpUtil.getRemortIP(request);
    }

    /**
     * 通过临时令牌获取当前登录用户信息
     *
     * @param tempToken
     * @return
     */
    public BorrowUser getLoginUser(String tempToken) {
        tempToken = TokenUtils.decryptTempToken(tempToken);
        BorrowUser bu = TokenUtils.decryptToken(StringUtils.getString(redisUtil.get(tempToken)));
        return bu;
    }

    /**
     * 得到页面传递的参数封装成map
     */

    public Map<String, String> getParameters(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        Map<String, String> p = new HashMap<String, String>();
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {

            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v = null;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    v = ((Object[]) value)[0];
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = (String) v;
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            //读取cookie
            p.putAll(ReadCookieMap(request));
            return p;
        }
        return p;
    }

    /**
     * 获得request中的参数
     *
     * @param request
     * @return string object类型的map
     */
    public HashMap<String, Object> getParametersO(HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {
            Map<String, Object> p = new HashMap<String, Object>();
            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v = null;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    if (((Object[]) value).length > 1) {
                        v = ((Object[]) value);
                    } else {
                        v = ((Object[]) value)[0];
                    }
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = ((String) v).trim();
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            hashMap.putAll(p);
            // 读取cookie
            hashMap.putAll(ReadCookieMap(request));

        }
        return hashMap;
    }


}
