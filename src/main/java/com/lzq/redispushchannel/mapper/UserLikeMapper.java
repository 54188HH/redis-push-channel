package com.lzq.redispushchannel.mapper;

import com.lzq.redispushchannel.config.dataSource.DataSource;
import com.lzq.redispushchannel.po.UserLike;
import com.lzq.redispushchannel.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:10
 */
@Mapper
@DataSource(name = "slave")
public interface UserLikeMapper extends MyMapper<UserLike> {
}
