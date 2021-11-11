package com.lzq.redispushchannel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@MapperScan(basePackages = "com.lzq.redispushchannel.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//屏蔽自带数据源自动加载
public class RedisPushChannelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisPushChannelApplication.class, args);
    }

}
