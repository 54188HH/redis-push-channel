package com.lzq.redispushchannel.controller;

import com.lzq.redispushchannel.po.User;
import com.lzq.redispushchannel.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lzq
 * @version 1.0
 * @date 2022/1/12 13:51
 * 关注和粉丝分析
 */
@RestController
public class FollowerController {
    @Autowired
    private FollowerService followerService;
    /**
     * 关羽 = 2 关注刘备 = 1
     * 张飞 = 3 关注刘备 = 1
     * userId = 1 == followId = 1
     *
     * @param userId = 关羽
     * @param followId = 张飞
     */
    @PostMapping("/user/follow")
    public String follow(Integer userId, Integer followId) {
        followerService.follow(userId, followId);
        return "用户:" + userId + "关注:" + followId + "成功!";
    }


    /**
     * 查询我的关注
     *
     * @param userId =关羽
     */
    @PostMapping("/user/followee")
    public List<User> listMyFollowee(Integer userId) throws IllegalAccessException {
        return followerService.listMyFollowee(userId);
    }

    @PostMapping("/user/maybe")
    public List<User> maybeUser(Integer userId) throws IllegalAccessException {
        return followerService.maybeUser(userId);
    }
    /**
     * 我的粉丝列表
     *
     * @param userId =关羽
     */
    @PostMapping("/user/fansList")
    public List<User> listMyFollower(Integer userId) throws IllegalAccessException {
        return followerService.listMyFollower(userId);
    }
}
