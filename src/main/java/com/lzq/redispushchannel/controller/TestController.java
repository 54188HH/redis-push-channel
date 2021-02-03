package com.lzq.redispushchannel.controller;

import com.lzq.redispushchannel.config.redisLock.RedisLockAnnotation;
import com.lzq.redispushchannel.config.redisLock.RedisLockTypeEnum;
import com.lzq.redispushchannel.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Book;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestController {
    @Autowired
    private RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @Test
    public void getData(){
        redisService.setGeo("location",Double.valueOf("120.071050"),Double.valueOf("30.282741"),"西湖区文体中心");
        redisService.setGeo("location",Double.valueOf("120.218862"),Double.valueOf("30.287623"),"杭州市职工文化中心");
        System.out.println("距离"+redisService.getDistance("location", "西湖区文体中心", "杭州市职工文化中心")
        );
    }
    @Test
    public void getLock(){
        System.out.println(testRedisLock());
    }
    @RedisLockAnnotation(typeEnum = RedisLockTypeEnum.ONE, lockTime = 3)
    public Book testRedisLock() {
        try {
            logger.info("睡眠执行前");
            Thread.sleep(10000);
            logger.info("睡眠执行后");
        } catch (Exception e) {
            // log error
            logger.info("has some error", e);
        }
        return null;
    }
}

