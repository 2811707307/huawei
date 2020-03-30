package com.application.huawei.controller.page;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @Auther: 10199
 * @Date: 2019/11/2 10:30
 * @Description: 前台页面跳转控制
 */
@Controller
public class FrontPageController {
    @GetMapping("/")
    public String index() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home() {
        return "front/home";
    }

    @GetMapping("/register")
    public String register(){
        return "front/register";
    }

    @GetMapping("/registerSuccess")
    public String registerSuccess() {
        return "front/registerSuccess";
    }

    @GetMapping("/login")
    public String login(){
        return "front/login";
    }

    @GetMapping("/frontLogout")
    public String logout(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()) {
            subject.logout();
        }
        return "redirect:home";
    }

    @GetMapping("/product")
    public String logout(){
        return "front/product";
    }

    @GetMapping("/search")
    public String search(){
        return "front/search";
    }

    @GetMapping("/buy")
    public String buy(){
        return "front/buy";
    }

    @GetMapping("/buyCart")
    public String buyCart(){
        return "front/buyCart";
    }

    @GetMapping("/pay")
    public String pay(){
        return "front/pay";
    }

    @GetMapping("/payed")
    public String payed() {
        return "front/paySuccess";
    }

    @GetMapping("/bought")
    public String bought() {
        return "front/bought";
    }

    @GetMapping("/review")
    public String review(){
        return "front/review";
    }
}
