package com.lzq.redispushchannel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/8 10:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyBO {
    //用户id
    private Integer id;
    //用户名称
    private String name;
    //距离
    private Double distance;
}
