package com.lzq.redispushchannel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzq.redispushchannel.mapper.UserMapper;
import com.lzq.redispushchannel.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lzq
 * @version 1.0
 * @date 2022/1/12 13:52
 */
@Service
public class FollowerService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserMapper userMapper;

    // 关注列表
    public static final String FOLLOWEE_SET_KEY = "followee:user:";
    // 粉丝列表
    public static final String FOLLOWER_SET_KEY = "follower:user:";


    /**
     * 关羽 = 2 关注刘备 = 1
     * 张飞 = 3 关注刘备 = 1
     * userId = 2 == followId = 1
     *
     * @param userId = 关羽
     * @param followId = 张飞
     */
    public void follow(Integer userId, Integer followId) {
        // 在刘备的粉丝集合列表中，把关羽和张飞加入到你的集合中
        redisService.sSet(FOLLOWER_SET_KEY + followId, String.valueOf(userId));
        // 关羽的和张飞关注集合列表中，增加刘备的信息
        redisService.sSet(FOLLOWEE_SET_KEY + userId, String.valueOf(followId));
    }


    /**
     * 查询我的关注
     *
     * @param userId = 关羽
     */
    public List<User> listMyFollowee(Integer userId) throws IllegalAccessException {

        Set<String> members = redisService.sGet(FOLLOWEE_SET_KEY + userId);
        return getUserInfos(members);
    }

    /**
     * 我的粉丝列表
     *
     * @param userId =关羽
     */
    public List<User> listMyFollower(Integer userId) throws IllegalAccessException {
        // 通过members方法，将关注列表的信息查询出来
        Set<String> members = redisService.sGet(FOLLOWER_SET_KEY + userId);
        return getUserInfos(members);
    }

    public List<User> maybeUser(Integer userId) throws IllegalAccessException {
        Set<String> strings = redisService.sDiff(FOLLOWER_SET_KEY + 2, FOLLOWER_SET_KEY + userId);
        return getUserInfos(strings);
    }
    /**
     * 把集合和前面的hash集合起来
     *
     * @param userInfos
     * @return
     */
    private List<User> getUserInfos(Set<String> userInfos) throws IllegalAccessException {
        // 创建用户集合
        List<User> userList = new ArrayList<>();
        // 循环关注列表的用户ID信息
        for (String userId : userInfos) {
            // 如果在缓存中没有找到对应的用户信息
            User user = JSONObject.parseObject(redisService.hget("user", "hash_user" + userId), User.class);
            if (null == user){
                User userDbCache = getUserDbCache(userId);
                if (null != userDbCache){
                    userList.add(userDbCache);
                }
            }else {
                userList.add(user);
            }

        }
        return userList;
    }


    /**
     * 从数据库去获取用户信息，并且把获取的用户新放入缓存HASH数据结构中
     *
     * @param userId
     * @return
     * @throws IllegalAccessException
     */
    public User getUserDbCache(String userId) throws IllegalAccessException {
        // 查询最新的用户信息放入到redis的hash中
        User user = userMapper.selectByPrimaryKey(userId);
        if (null == user){
            return null;
        }
        redisService.hset("user","hash_user"+userId, JSON.toJSONString(user));
        return user;
    }
}
