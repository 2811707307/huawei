package com.application.huawei.controller.admin;

import com.application.huawei.pojo.User;
import com.application.huawei.service.UserService;
import com.application.huawei.util.PageUtil;
import com.application.huawei.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;

/**
 * 后台管理员登录、用户管理相关接口
 *
 * @Auther: 10199
 * @Date: 2020/1/20 17:19
 */

@RestController
public class AdminUserController {
    @Resource
    UserService userService;

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

    @GetMapping("/users")
    public PageUtil<User> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value="size", defaultValue = "5") int size) throws Exception{
        start = start < 0?0:start;
        PageUtil<User> page = userService.list(start, size, 5);
        return page;
    }
}
