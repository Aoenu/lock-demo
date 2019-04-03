package com.hand.lock.simple;

import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/03
 */
public class RedissonManager {
    private static Config config = new Config();

    /**
     * 声明redisson对象
     */
    private static Redisson redisson = null;

    /**
     * 实例化redisson
     */
    static {
        config.useSingleServer().setAddress("127.0.0.1:6379");
        //得到redisson对象
        redisson = (Redisson) Redisson.create(config);
    }

    /**
     * 获取redisson对象的方法
     *
     * @return
     */
    public static Redisson getRedisson() {
        return redisson;
    }
}
