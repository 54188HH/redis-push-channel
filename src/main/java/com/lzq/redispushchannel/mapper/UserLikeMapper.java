package com.lzq.redispushchannel.mapper;

import com.lzq.redispushchannel.po.UserLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:10
 */
@Mapper
public interface UserLikeMapper extends tk.mybatis.mapper.common.Mapper<UserLike>, tk.mybatis.mapper.common.MySqlMapper<UserLike> {
}
