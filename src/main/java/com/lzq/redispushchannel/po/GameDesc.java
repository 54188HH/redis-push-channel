package com.lzq.redispushchannel.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @author lzq
 * @version 1.0
 * @date 2022/1/6 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDesc {
    private String issueNo;
    private String redResult;
    private String blueResult;
}
