package com.application.huawei.util;

/**
 * @Auther: 10199
 * @Date: 2019/12/11 16:16
 * @Description: SpringBoot 的缓存机制通过切面编程aop实现，在类里面调用类本身的方法不会触发
 *                缓存，通过这个工具类故意出发aop
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private SpringContextUtil() {

    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}