package com.lzq.redispushchannel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/6 14:33
 */
@RestController
public class PushMessageController {
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/pushMessage")
    public void pushMessage(String message){
        redisTemplate.convertAndSend("messagepush",message);
        redisTemplate.convertAndSend("messagepush1",message);
    }
}
