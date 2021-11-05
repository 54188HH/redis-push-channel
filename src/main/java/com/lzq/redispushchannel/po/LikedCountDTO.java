package com.lzq.redispushchannel.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikedCountDTO {
    private Long id;
    private Integer count;
}
