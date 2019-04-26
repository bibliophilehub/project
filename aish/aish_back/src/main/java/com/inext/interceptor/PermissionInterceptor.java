package com.inext.interceptor;

import com.inext.constants.Constants;
import com.inext.constants.SysConstant;
import com.inext.entity.BackUser;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 权限拦截器
 * 开发人员：jzhang
 * 创建时间：2017-04-28 下午 14:52
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BackUser backUser = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        if (backUser != null) {
            if (backUser.getAccount().equalsIgnoreCase("admin") || backUser.getAccount().equalsIgnoreCase("superUser") || backUser.getAccount().equalsIgnoreCase("tangtaijun")) {//超级管理员不要判断 防止数据库表出现异常不能使用
                return true;
            }

            String URI = request.getRequestURI();
            String toMath = URI.replaceFirst(request.getContextPath() + "/", "");

            //区分是否是导流渠道商
            if("02".equals(backUser.getUserType())) {
                if("/diversion/statistics".equals(URI) || "/diversion/statisticsHis".equals(URI)) {
                    return true;
                }
            } else {
                if (SysConstant.ModuleMap != null) {
                    if (SysConstant.ModuleMap.get(URI) != null || SysConstant.ModuleMap.get(toMath) != null) {
                        if (backUser.getMappinfo() != null) {
                            if (backUser.getMappinfo().get(URI) != null || backUser.getMappinfo().get(toMath) != null) {
                                return true;
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }

            /*
            List<PermissionInfo> pinfo=backUser.getPinfo();
            if(pinfo!=null&&pinfo.size()>0){
                for (PermissionInfo info:pinfo){
                    String url=info.getUrl();
                    if(url.equals(toMath)||url.equals(URI)){
                        return true;
                    }
                }
            }*/
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.write("<script language='javascript' tag='login_failure'>");
        out.write("		alert('没有得到授权执行此项操作');");
        out.write("</script>");
        out.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        BackUser backUser = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        if (backUser != null) {
            if (backUser.getAccount().equalsIgnoreCase("admin") || backUser.getAccount().equalsIgnoreCase("superUser")) {//超级管理员不要判断 防止数据库表出现异常不能使用

            } else if (modelAndView != null) {
                modelAndView.addObject("userMappinfo", backUser.getMappinfo());
            }
        }
    }

}
