package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.entity.BackUser;
import com.inext.entity.BorrowUser;
import com.inext.utils.IpUtil;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


public class BaseController {
    public static final String MESSAGE = "message";
    @Resource
    private ImageCaptchaService captchaService;


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
     * 得到 session
     *
     * @return
     */
    protected HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
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
     * 得到session中的admin user对象
     */
    public BackUser loginAdminUser(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        BackUser backUser = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        return backUser;
    }

    /**
     * 得到session中的borrowUser对象
     */
    public BorrowUser loginFrontUser(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        BorrowUser zbUser = (BorrowUser) request.getSession().getAttribute(Constants.YIDAI_WEB_USER);
        return zbUser;
    }

    /**
     * 验证码
     *
     * @param request
     * @param response
     * @return
     */
    public boolean validateSubmit(HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            return captchaService.validateResponseForID(request.getSession(false).getId(), request.getParameter("captcha").toLowerCase());
        } catch (Exception e) {
            return false;
        }
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

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    /**
     * 生成查询页面中日期的初始范围,30天
     * 是否向前查询: 是,结束日期为当前时间; 否,开始日期为当前时间
     * BorrowUserController没有继承此类,日期限制另外处理
     *
     * @return
     */
    protected Map<Integer, String> getInitQueryStartDate(boolean isBefore) {
        String startDate = "", endDate = "";
        Map<Integer, String> map = new HashMap<>();
        Calendar c = Calendar.getInstance();
        if (isBefore) {
            endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            map.put(2, endDate);
            c.add(Calendar.DATE, -30);
            startDate = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
            map.put(1, startDate);
        } else {
            startDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            map.put(1, startDate);
            c.add(Calendar.DATE, 30);
            endDate = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
            map.put(2, endDate);
        }
        return map;
    }

    /**
     * 生成查询页面中日期的初始范围
     * isBefore 是否向前查询: 是,结束日期为当前时间; 否,开始日期为当前时间
     * days 间隔的天数
     * BorrowUserController没有继承此类,日期限制另外处理
     *
     * @return
     */
    protected Map<Integer, String> getInitQueryStartDate(boolean isBefore, Integer days) {
        String startDate = "", endDate = "";
        Map<Integer, String> map = new HashMap<>();
        Calendar c = Calendar.getInstance();
        if (isBefore) {
            endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            map.put(2, endDate);
            c.add(Calendar.DATE, -days);
            startDate = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
            map.put(1, startDate);
        } else {
            startDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            map.put(1, startDate);
            c.add(Calendar.DATE, days);
            endDate = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
            map.put(2, endDate);
        }
        return map;
    }

    /**
     * 初始化pageInfo的分页
     */
    protected PageInfo getPage() {
        PageInfo page = new PageInfo<>(null);
        page.setPageNum(1);
        page.setPageSize(10);
        return page;
    }


}
