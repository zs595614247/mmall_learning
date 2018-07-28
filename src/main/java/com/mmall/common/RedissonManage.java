package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class RedissonManage {

    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    @PostConstruct
    private void init() {
        try {
            Config config = Config.fromJSON(new File("properties/redisson.json"));
            redissonClient = Redisson.create(config);
        } catch (IOException e) {
           log.error("redis init error",e);
        }
    }



}
