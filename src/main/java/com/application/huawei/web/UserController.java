package com.application.huawei.web;

import com.application.huawei.pojo.User;
import com.application.huawei.service.UserService;
import com.application.huawei.util.PageUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 13:35
 * @Description:
 */

@RestController
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/users")
    public PageUtil<User> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value="size", defaultValue = "5") int size) throws Exception{
        start = start < 0?0:start;
        PageUtil<User> page = userService.list(start, size, 5);
        return page;
    }
}
