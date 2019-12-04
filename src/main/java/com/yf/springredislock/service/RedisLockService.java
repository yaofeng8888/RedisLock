package com.yf.springredislock.service;

/**
 * @author yaofeng
 * @date 2019/11/4 15:44
 */
public interface RedisLockService {
    String productLockByRedis();

    String productLockByRedisson() throws InterruptedException;
}
