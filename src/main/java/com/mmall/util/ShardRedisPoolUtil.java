package com.mmall.util;

import com.mmall.common.ShardRedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * @author zs595
 */
@Slf4j
public class ShardRedisPoolUtil {

    /**
     * 删除key
     * @param key key
     * @return value
     */
    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
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
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 添加key-value
     * @param key key
     * @param value value
     * @return old value
     */
    public static String getSet(String key, String value) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getSet key:{} value:{} error",key,value,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 添加key-value,当且仅当key不存在时,才会成功,成功返回1,失败返回0
     * @param key key
     * @param value value
     * @return Status code reply
     */
    public static Long setNx(String key, String value) {
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setNx key:{} value:{} error",key,value,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
        }
        return result;
    }

    /**
     * 查询key的值
     * @param key key
     * @return value
     */
    public static String get(String key) {
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
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
        ShardedJedis jedis = null;
        Long result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.expire(key,expireDate);
        } catch (Exception e) {
            log.error("set key:{} error",key,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
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
        ShardedJedis jedis = null;
        String result;
        try {
            jedis = ShardRedisPool.getJedis();
            result = jedis.setex(key, expireDate, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            return null;
        }finally {
            ShardRedisPool.returnResource(jedis);
        }
        return result;
    }

}
