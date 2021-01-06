package com.lzq.redispushchannel.config;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/6 14:23
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**redis 消息处理器*/
@Component
public class MessageReceive {

    @Autowired
    private MessageReceiveHandler messageReceiveHandler;
    /**接收消息的方法*/
    public void receiveMessage(String message){
        messageReceiveHandler.messagePush(message);
    }

}
