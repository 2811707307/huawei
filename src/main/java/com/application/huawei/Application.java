package com.application.huawei;

import com.application.huawei.util.PortUtil;
import com.application.huawei.util.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
//@EnableElasticsearchRepositories(basePackages = "com.application.huawei.es")
//@EnableJpaRepositories(basePackages = {"com.application.huawei.dao","com.application.huawei.pojo"})
public class Application  {
    static {
        PortUtil.checkPort("127.0.0.1", 6379, "Redis服务", true);
//        PortUtil.checkPort("116.62.161.119", 9300, "ElasticSearch", true);
        RedisUtil.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
