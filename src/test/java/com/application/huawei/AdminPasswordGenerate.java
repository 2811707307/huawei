package com.application.huawei;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @Auther: 10199
 * @Date: 2020/2/2 20:07
 */
public class AdminPasswordGenerate {

    public static void main(String[] args) {
        String password = "admin";

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String algorithmName = "md5";
        password = new SimpleHash(algorithmName, password, salt, 2).toString();
        System.out.println("应当插入User表的数据是：账号 + , " + password + ", " + salt);
        System.out.println("登录账号为 admin，密码为admin");
    }
}
