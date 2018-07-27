package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class ShardRedisPool {
    /**
     * Jedis连接池
     */
    private static ShardedJedisPool jedisPool;

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
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    /**
     * redisServer port
     */
    private static Integer redis1Port = PropertiesUtil.getIntegerProperty("redis1.port");
    /**
     * redisServer password
     */
    private static String redis1Password = PropertiesUtil.getProperty("redis1.password");

    /**
     * redisServer ip
     */
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    /**
     * redisServer port
     */
    private static Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port");
    /**
     * redisServer password
     */
    private static String redis2Password = PropertiesUtil.getProperty("redis2.password");

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
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redis1Ip, redis1Port, 2000);
        jedisShardInfo1.setPassword(redis1Password);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redis2Ip, redis2Port, 2000);
        jedisShardInfo2.setPassword(redis2Password);
        List<JedisShardInfo> shardInfos = new ArrayList<>();
        shardInfos.add(jedisShardInfo1);
        shardInfos.add(jedisShardInfo2);
        jedisPool = new ShardedJedisPool(config, shardInfos, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }

    public static void returnResource(ShardedJedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
