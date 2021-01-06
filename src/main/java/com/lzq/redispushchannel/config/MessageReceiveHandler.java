package com.lzq.redispushchannel.config;

import org.springframework.stereotype.Component;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/6 14:23
 */
@Component
public class MessageReceiveHandler {
    public void messagePush(String message){
        System.out.println("收到新消息："+message);
    }
}

