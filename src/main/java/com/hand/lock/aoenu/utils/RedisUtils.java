package com.hand.lock.aoenu.utils;

import com.hand.lock.aoenu.annotation.RedisLockKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */
public class RedisUtils {
    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static RedisUtils redisUtils;

    private static RedissonClient redissonClient;

    private RedisUtils() {
    }

    /**
     * 提供单例模式
     *
     * @return
     */
    public static RedisUtils getInstance() {
        if (redisUtils == null) {
            synchronized (RedisUtils.class) {
                if (redisUtils == null) {
                    redisUtils = new RedisUtils();
                }
            }
        }
        return redisUtils;
    }

    /**
     * @param address
     * @param pass
     * @return
     */
    public static RedissonClient createClient(String address, String pass) {
        if (redissonClient == null) {
            synchronized (RedisUtils.class) {
                if (redissonClient == null) {
                    Config config = new Config();
                    SingleServerConfig singleServerConfig = config.useSingleServer();
                    singleServerConfig.setAddress(address);
                    singleServerConfig.setPassword(pass);
                    redissonClient = RedisUtils.getInstance().getRedisson(config);
                }
            }
        }
        return redissonClient;
    }

    /**
     * 使用config创建Redisson
     * Redisson是用于连接Redis Server的基础类
     *
     * @param config
     * @return
     */
    private RedissonClient getRedisson(Config config) {
        RedissonClient redisson = Redisson.create(config);
        logger.info("成功连接Redis Server");
        return redisson;
    }

    /**
     * 关闭Redisson客户端连接
     *
     * @param redisson
     */
    public static void closeRedisson(RedissonClient redisson) {
        redisson.shutdown();
        logger.info("成功关闭Redis Client连接");
    }

    /**
     * 获取包括方法参数上的key
     *
     * @param point
     * @param lockKey
     * @return
     */
    public String getLockKey(ProceedingJoinPoint point, String lockKey) {
        try {
            lockKey = "RedisLock:" + lockKey;
            Object[] args = point.getArgs();
            if (args != null && args.length > 0) {
                MethodSignature methodSignature = (MethodSignature) point.getSignature();
                Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
                SortedMap<Integer, String> keys = new TreeMap<>();
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    RedisLockKey redisLockKey = getAnnotation(RedisLockKey.class, parameterAnnotations[i]);
                    if (redisLockKey != null) {
                        Object arg = args[i];
                        if (arg != null) {
                            keys.put(redisLockKey.order(), arg.toString());
                        }
                    }
                }
                if (keys != null && keys.size() > 0) {
                    for (String key : keys.values()) {
                        lockKey += ":" + key;
                    }
                }
            }

            return lockKey;
        } catch (Exception e) {
            logger.error("getLockKey error.", e);
        }
        return null;
    }

    /**
     * 获取注解类型
     *
     * @param annotationClass
     * @param annotations
     * @param <T>
     * @return
     */
    private static <T extends Annotation> T getAnnotation(final Class<T> annotationClass, final Annotation[] annotations) {
        if (annotations != null && annotations.length > 0) {
            for (final Annotation annotation : annotations) {
                if (annotationClass.equals(annotation.annotationType())) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }

    /**
     * 获取锁
     *
     * @param redissonClient
     * @param lockKey
     * @return
     */
    public RLock getRLock(RedissonClient redissonClient, String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        return rLock;
    }
}
