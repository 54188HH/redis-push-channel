package com.lzq.redispushchannel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "com.lzq.redispushchannel.mapper")
public class RedisPushChannelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisPushChannelApplication.class, args);
    }

}
