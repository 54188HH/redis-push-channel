package com.lzq.redispushchannel.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/3/10 15:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String mobile;
    private int age;
}
