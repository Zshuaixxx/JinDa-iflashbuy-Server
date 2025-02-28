package com.sky.utils;

/**
 * @author 帅的被人砍
 * @create 2025-02-28 12:06
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 */
@Component
public class RedisLockUtil {

    /**
     * 获取分布式锁
     * @param redisTemplate Redis 模板
     * @param key 锁的键
     * @param value 锁的值
     * @param expireTime 锁的过期时间
     * @return 是否获取锁成功
     */
    public boolean getLock(RedisTemplate<String, String> redisTemplate, String key, String value, long expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 释放分布式锁
     * @param redisTemplate Redis 模板
     * @param key 锁的键
     * @param value 锁的值
     */
    public void releaseLock(RedisTemplate<String, String> redisTemplate, String key, String value) {
        if (value.equals(redisTemplate.opsForValue().get(key))) {
            redisTemplate.delete(key);
        }
    }
}
