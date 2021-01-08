package com.lzq.redispushchannel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/8 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoVo {
    //商家id
    private String number;
    //距离
    private String distance;
}
