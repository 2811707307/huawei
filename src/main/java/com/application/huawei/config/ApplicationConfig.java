package com.application.huawei.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */

@Configuration
public class ApplicationConfig implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler指访问路径，addResourceLocations为本地路径
        //此处开放静态目录static的访问，从而让css等文件能正确引用
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/");
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + System.getProperty("user.home") + "/huawei/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }


}
