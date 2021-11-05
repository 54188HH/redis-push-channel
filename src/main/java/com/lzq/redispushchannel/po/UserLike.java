package com.lzq.redispushchannel.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLike {
    //主键id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //被点赞的用户的id
    private String likedUserId;

    //点赞的用户的id
    private String likedPostId;

    //点赞状态，0取消，1点赞
    private Integer status;

    private Date createTime;

    private Date updateTime;

    public UserLike(String likedUserId, String likedPostId, Integer status) {
        this.likedUserId = likedUserId;
        this.likedPostId = likedPostId;
        this.status = status;
        this.createTime = new Date();
    }
}
