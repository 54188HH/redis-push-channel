package com.lzq.redispushchannel.mapper;

import com.lzq.redispushchannel.dto.TbStadium;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/8 11:42
 */
@Mapper
public interface StadiumMapper {
    @Select("select * from tb_stadium where stadium_id in (1,48,49,52,5380,87,88,90,91,92,93,97,98,99,100,101,102,103,105,106,110,112,113)")
    List<TbStadium> queryAll();
}
