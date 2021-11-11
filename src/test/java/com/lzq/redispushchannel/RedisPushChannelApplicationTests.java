package com.lzq.redispushchannel;

import com.lzq.redispushchannel.mapper.UserLikeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisPushChannelApplicationTests {
    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private ZhihuTask zhihuTask;
    @Test
    void contextLoads()  {

        zhihuTask
                .crawl();


    }



}
