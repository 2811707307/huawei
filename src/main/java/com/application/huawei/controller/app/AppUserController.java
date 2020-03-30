package com.application.huawei.controller.app;

import com.application.huawei.pojo.User;
import com.application.huawei.service.UserService;
import com.application.huawei.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;

/**
 * @author: hpj
 * @Date: 2020/3/30 11:44
 */

@RestController
public class AppUserController {
    @Resource
    UserService userService;

    @PostMapping("/frontRegister")
    public Object register(@RequestBody User user) {
        String name = user.getName();
        String password = user.getPassword();
        //转义特殊字符
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if (exist) {
            String message = "用户名已经被使用,不能使用";
            return ResultUtil.fail(message);
        }

        //使用Shiro 盐加密
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return ResultUtil.success();
    }

    @PostMapping("/frontLogin")
    public Object login(@RequestBody User userParam) {
        String name = userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        //登录时使用Shiro的方式进行校验
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword());
        try {
            subject.login(token);
            User user =  userService.getByName(name);
            subject.getSession().setAttribute("user", user);
            return ResultUtil.success();
        }catch (AuthenticationException e){
            String message = "账号或密码错误";
            return ResultUtil.fail(message);
        }
    }

    @GetMapping("/frontCheckLogin")
    public Object checkLogin() {
        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated() && subject.getSession().getAttribute("user") != null ) {
            return ResultUtil.success();
        } else {
            return ResultUtil.fail("未登录");
        }
    }
}
