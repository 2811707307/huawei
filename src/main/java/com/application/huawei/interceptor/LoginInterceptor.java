package com.application.huawei.interceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: 10199
 * @Date: 2019/12/15 10:06
 * @Description:
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] requireAuthPages = new String[]{
                "/buy",
                "/pay",
                "/payed",
                "/cart",
                "/bought",
                "/frontBuyOne",
                "/frontBuy",
                "/frontAddCart",
                "/frontCart",
                "/frontChangeOrderItem",
                "/frontDeleteOrderItem",
                "/frontCreateOrder",
                "/frontPayed",
                "/frontBought",
                "/frontConfirmPay",
                "/frontOrderConfirmed",
                "/frontDeleteOrder",
                "/frontReview",
                "/frontDoReview"
        };

        String uri = request.getRequestURI();

        if(begingWith(uri, requireAuthPages)){
            if(SecurityUtils.getSubject().getSession().getAttribute("user") == null) {
                response.sendRedirect("login");
                return false;
            }
        }
        return true;
    }

    private boolean begingWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            if(StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
