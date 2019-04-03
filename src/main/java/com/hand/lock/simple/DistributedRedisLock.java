package com.hand.lock.simple;

import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * This is Description
 * 这种锁的方式比较简单 也比较不好用不是注解的方式
 * 用法：   //加锁
 *         DistributedRedisLock.acquire(key);
 *         //执行具体业务逻辑
 *         doSomething
 *         //释放锁
 *         DistributedRedisLock.release(key);
 *        //返回结果
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */
public class DistributedRedisLock {
    /**
     * 从配置类中获取redisson对象
     */
    private static Redisson redisson = RedissonManager.getRedisson();

    private static final String LOCK_TITLE = "redisLock:";

    /**
     * 加锁
     * @param lockName
     * @return
     */
    public static boolean acquire(String lockName){
        //声明key对象
        String key = LOCK_TITLE + lockName;
        //获取锁对象
        RLock myLock = redisson.getLock(key);
        //加锁，并且设置锁过期时间，防止死锁的产生
        myLock.lock(2, TimeUnit.MINUTES);
        System.err.println("======lock======"+Thread.currentThread().getName());
        //加锁成功
        return  true;
    }

    /**
     * 锁的释放
     * @param lockName
     */
    public static void release(String lockName){
        //必须是和加锁时的同一个key
        String key = LOCK_TITLE + lockName;
        //获取所对象
        RLock myLock = redisson.getLock(key);
        //释放锁（解锁）
        myLock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }
}
