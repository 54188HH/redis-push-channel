package com.lzq.redispushchannel.utils;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:03
 */
public class RedisKeyUtils {
    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。格式 222222::333333
     * @param likedUserId 被点赞的人id
     * @param likedPostId 点赞的人的id
     * @return
     * create table `user_like`(
     *     `id` int not null auto_increment,
     *     `liked_user_id` varchar(32) not null comment '被点赞的用户id',
     *     `liked_post_id` varchar(32) not null comment '点赞的用户id',
     *     `status` tinyint(1) default '1' comment '点赞状态，0取消，1点赞',
     *     `create_time` timestamp not null default current_timestamp comment '创建时间',
     *   `update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
     *     primary key(`id`),
     *     INDEX `liked_user_id`(`liked_user_id`),
     *     INDEX `liked_post_id`(`liked_post_id`)
     * ) comment '用户点赞表';
     */
    public static String getLikedKey(String likedUserId, String likedPostId){
        StringBuilder builder = new StringBuilder();
        builder.append(likedUserId);
        builder.append("::");
        builder.append(likedPostId);
        return builder.toString();
    }
}