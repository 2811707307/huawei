package com.application.huawei.web;

import com.application.huawei.pojo.User;
import com.application.huawei.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

/**
 * @Auther: 10199
 * @Date: 2020/1/20 17:19
 */

@RestController
public class AdminController {
    @PostMapping("/adminLogin")
    public Object login(@RequestBody User user) {
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        String password = user.getPassword();
        password = HtmlUtils.htmlEscape(password);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);

        try {
            subject.login(token);
            subject.getSession().setAttribute("admin", user);
            return ResultUtil.success();
        } catch (Exception e) {
            return ResultUtil.fail("账号或密码错误");
        }
    }

    @GetMapping("/admin_logout")
    public Object logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return null;
    }
}
