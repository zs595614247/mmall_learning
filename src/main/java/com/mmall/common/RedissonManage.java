package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManage {

    private RedissonClient redissonClient;

    /**
     * redisServer ip
     */
    private static String redisIp = PropertiesUtil.getProperty("redis1.ip");
    /**
     * redisServer port
     */
    private static Integer redisPort = PropertiesUtil.getIntegerProperty("redis1.port");
    /**
     * redisServer password
     */
    private static String redisPassword = PropertiesUtil.getProperty("redis1.password");

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    @PostConstruct
    private void init() {
        try {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://"+redisIp+":"+redisPort).setPassword(redisPassword).setDatabase(1);
            redissonClient = Redisson.create(config);
        } catch (Exception e) {
           log.error("redis init error",e);
        }
    }



}
