package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;

public class RedisPool {

    /**
     * Jedis连接池
     */
    private static JedisPool jedisPool;

    /**
     * 最大连接数
     */
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total", "20");
    /**
     * 最大idle(空闲)连接数
     */
    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle", "10");
    /**
     * 最小idle(空闲)连接数
     */
    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle", "2");
    /**
     * 在borrow一个jedis实例的时候是否要进行一个验证操作,如果设置为true,则每次Jedis实例都是看可用的
     */
    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.test.borrow", "true");
    /**
     * 在return一个jedis实例的时候是否要进行一个验证操作,如果设置为true,则放回JedisPool的Jedis实例都是可用的
     */
    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.test.return", "true");
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

    static {
        initPool();
    }

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候是否阻塞,false会抛出异常,true阻塞直到连接超时,默认为true
        config.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(config,redisIp,redisPort,2000,redisPassword);
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
