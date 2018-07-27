package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author zs595
 */
@Slf4j
public class RedisPoolUtil {

    /**
     * 删除key
     * @param key key
     * @return value
     */
    public static Long del(String key) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            return null;
        }finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 添加key-value
     * @param key key
     * @param value value
     * @return Status code reply
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            return null;
        }finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 添加key-value,当且仅当key不存在是才会成功
     * @param key key
     * @param value value
     * @return Status code reply
     */
    public static Long setNx(String key, String value) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            return null;
        }finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 查询key的值
     * @param key key
     * @return value
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            return null;
        }finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置key的有效期,单位秒
     * @param key key
     * @param expireDate 有效期
     * @return 超时返回1,未设置返回0
     */
    public static Long expire(String key, int expireDate) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,expireDate);
        } catch (Exception e) {
            log.error("set key:{} error",key,e);
            return null;
        }finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 添加带有expiryTime的key-value
     * @param key key
     * @param value value
     * @param expireDate key的有效期 单位 s(second)
     * @return Status code reply
     */
    public static String setExpire(String key, String value,int expireDate) {
        Jedis jedis = null;
        String result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, expireDate, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            return null;
        }finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }
}
