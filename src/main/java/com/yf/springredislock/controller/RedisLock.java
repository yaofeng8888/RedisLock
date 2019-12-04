package com.yf.springredislock.controller;

import com.yf.springredislock.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author yaofeng
 * @date 2019/11/4 15:40
 */
@Controller
@RequestMapping("lock")
public class RedisLock {

    private final RedisLockService redisLockService;

    @Autowired
    public RedisLock(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    @RequestMapping("deduct_lock")
    public String deductLock(){
        return  redisLockService.productLockByRedis();
    }
}
