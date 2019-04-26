package com.inext.utils;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {
    /**
     * 删除session
     *
     * @param request
     * @param sessionName
     */
    public static void removeSeesion(HttpServletRequest request, String sessionName) {
        request.getSession().removeAttribute(sessionName);
    }

}
