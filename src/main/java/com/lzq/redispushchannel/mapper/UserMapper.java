package com.lzq.redispushchannel.mapper;

import com.lzq.redispushchannel.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:53
 */
@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<User>, tk.mybatis.mapper.common.MySqlMapper<User> {
}
