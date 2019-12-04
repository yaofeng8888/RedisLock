package com.yf.springredislock.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.yf.springredislock.service.RedisLockService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yaofeng
 * @date 2019/11/4 15:45
 */
@Service
public class RedisLockServiceImpl implements RedisLockService {

    private final StringRedisTemplate stringRedisTemplate;
    private final Redisson redisson;
    private final String lockKey = "productId";

    @Autowired
    public RedisLockServiceImpl(StringRedisTemplate stringRedisTemplate, Redisson redisson) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisson = redisson;
    }

    @Override
    public String productLockByRedis() {
        String clientId = "";
        try {
            clientId = UUID.randomUUID().toString().replaceAll("-","");
            Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId,10, TimeUnit.SECONDS);
            if (!aBoolean){
                //也可以排队等待
                return "error";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock>0){
                int redisStock = stock--;
                stringRedisTemplate.opsForValue().set("stock",redisStock+"");
                System.out.println("扣减成功"+redisStock+"");
            }else {
                System.out.println("扣减失败,库存不足");
            }
        }finally {
            if (stringRedisTemplate.opsForValue().get(lockKey).equals(clientId)){
                stringRedisTemplate.delete(lockKey);
            }
        }
        return "end";
    }


    @Override
    public String productLockByRedisson() throws InterruptedException {
        RLock lock = redisson.getLock(lockKey);
        try {
            lock.tryLock(30,TimeUnit.SECONDS);
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock>0){
                int redisStock = stock--;
                stringRedisTemplate.opsForValue().set("stock",redisStock+"");
                System.out.println("扣减成功"+redisStock+"");
            }else {
                System.out.println("扣减失败,库存不足");
            }
        }finally {
            lock.unlock();
        }
        return "end";
    }
}
