package com.hand.lock.aoenu.annotation;

import java.lang.annotation.*;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLockKey {
    /**
     * key的拼接顺序规则
     */
    int order() default 0;
}
