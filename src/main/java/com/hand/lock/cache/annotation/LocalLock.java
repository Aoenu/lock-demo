package com.hand.lock.cache.annotation;

import java.lang.annotation.*;

/**
 * 锁的注解
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LocalLock {

    /**
     * @author
     */
    String key() default "";

    /**
     * 过期时间  由于用的 guava 暂时忽略这属性 集成 redis 需要用到
     *
     * @author
     */
    int expire() default 5;
}
