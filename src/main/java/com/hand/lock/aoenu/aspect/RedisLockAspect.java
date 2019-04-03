package com.hand.lock.aoenu.aspect;

import com.hand.lock.aoenu.utils.RedisUtils;
import com.hand.lock.aoenu.annotation.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */
@Component
@Aspect
public class RedisLockAspect {
    private static Logger logger = LoggerFactory.getLogger(RedisLockAspect.class);

    @Value("${spring.redis.host}:${spring.redis.port}")
    private String address;

    @Around("execution(public * *(..)) && @annotation(com.hand.lock.aoenu.annotation.RedisLock)")
    public Object lock(ProceedingJoinPoint point) throws Throwable {
        RLock lock = null;
        Object object = null;
        logger.info("into Aspect!");
        try {
            RedisLock redisLock = getDistRedisLockInfo(point);
            RedisUtils redisUtils = RedisUtils.getInstance();
            RedissonClient redissonClient = RedisUtils.createClient(address, null);
            String lockKey = redisUtils.getLockKey(point, redisLock.lockKey());

            lock = redisUtils.getRLock(redissonClient, lockKey);
            if (lock != null) {
                Boolean status = lock.tryLock(redisLock.timeUnit(), redisLock.expire(), TimeUnit.MILLISECONDS);
                if (status) {
                    object = point.proceed();
                }
            }
        } finally {
            // 如果演示的话需要注释该代码;实际应该放开
//            if (lock != null) {
//                lock.unlock();
//            }
        }
        return object;
    }

    /**
     * 获取RedisLock锁注解信息
     * @param point
     * @return
     */
    private RedisLock getDistRedisLockInfo(ProceedingJoinPoint point) {
        try {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            return method.getAnnotation(RedisLock.class);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return null;
    }
}
