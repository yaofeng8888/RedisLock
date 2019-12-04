package com.yf.springredislock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringRedisLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisLockApplication.class, args);
    }


    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(1);
        return (Redisson)Redisson.create(config);
    }
}
