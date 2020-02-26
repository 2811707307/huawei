package com.application.huawei.interceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Auther: 10199
 * @Date: 2019/12/15 10:06
 * @Description:
 */

public class AdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] requireAuthPages = new String[]{
                "/admin_category_list",
                "/admin_user_list",
                "/admin_order_list"
        };

        String uri = request.getRequestURI();
        if (uri.equals("/"))
            return true;
        else if(begingWith(requireAuthPages, uri)){
            if(SecurityUtils.getSubject().getSession().getAttribute("admin") == null) {
                response.sendRedirect("admin_login");
                return false;
            }
        }
        return true;
    }

    private boolean begingWith(String[] requiredAuthPages, String page) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            if(StringUtils.startsWith(requiredAuthPage, page)) {
                result = true;
                break;
            }
        }
        return result;
    }

}
