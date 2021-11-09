package com.lzq.redispushchannel;

import com.alibaba.druid.pool.DruidDataSource;
import com.lzq.redispushchannel.mapper.SysRoleDeptMapper;
import com.lzq.redispushchannel.mapper.UserLikeMapper;
import com.lzq.redispushchannel.po.SysRoleDept;
import com.lzq.redispushchannel.po.UserLike;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class RedisPushChannelApplicationTests {
    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private SysRoleDeptMapper deptMapper;

    @Test
    void contextLoads() throws SQLException {

        UserLike userLike = userLikeMapper.selectByPrimaryKey(1);
        System.out.println(userLike.toString());


    }



}
