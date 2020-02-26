package com.application.huawei.config;

import com.application.huawei.interceptor.AdminLoginInterceptor;
import com.application.huawei.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: 10199
 * @Date: 2019/12/17 11:38
 * @Description: 配置自定义拦截器
 */
@Configuration
public class WebMvcConfigurerAdapter implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public AdminLoginInterceptor getAdminLoginInterceptor(){
        return new AdminLoginInterceptor();
    }

    //注册与配置拦截器生效的域
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getAdminLoginInterceptor()).addPathPatterns("/**");

    }
}
