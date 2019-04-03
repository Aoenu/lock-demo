package com.hand.lock.aoenu.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {
    /**
     * 锁的key
     * redis key的拼写规则为 "DistRedisLock+" + lockKey + @RedisLockKey
     * @return
     */
    String lockKey();

    /**
     * 持锁时间
     * 单位毫秒,默认5秒
     * @return
     */
    long expire() default 10 * 1000;

    /**
     * 没有获取到锁时，等待时间
     * @return
     */
    long timeUnit() default 120 * 1000;
}
