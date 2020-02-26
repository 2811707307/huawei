package com.application.huawei.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    /**
     * 程序启动时刷新缓存，开发模式使用
     */
    public static void init(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "116.62.161.119", 6379, 2000, "handsomehuang");
        Jedis jedis = jedisPool.getResource();
        jedis.flushAll();
    }
}
