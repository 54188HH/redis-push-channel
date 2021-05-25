package com.lzq.redispushchannel.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzq.redispushchannel.config.redisLock.RedisLockAnnotation;
import com.lzq.redispushchannel.config.redisLock.RedisLockTypeEnum;
import com.lzq.redispushchannel.dto.*;
import com.lzq.redispushchannel.mapper.StadiumMapper;
import com.lzq.redispushchannel.po.User;
import com.lzq.redispushchannel.service.NearbyBizService;
import com.lzq.redispushchannel.service.RedisKitService;
import com.lzq.redispushchannel.service.RedisService;
import com.lzq.redispushchannel.service.RedisUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/6 14:33
 */
@RestController
public class PushMessageController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private NearbyBizService bizService;
    @Autowired
    private StadiumMapper stadiumMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisKitService redisKitService;
    @Autowired
    private RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(PushMessageController.class);

    @GetMapping("/pushMessage")
    public void pushMessage(String message) {
        redisTemplate.convertAndSend("messagepush", message);
        redisTemplate.convertAndSend("messagepush1", message);
    }

    // 附近的人
    @RequestMapping(value = "nearby")
    public Result<List<NearbyBO>> nearby(@Valid NearbyPO paramObj) {
        return bizService.nearby(paramObj);
    }

    // 将商家存入到redis中
    @RequestMapping(value = "redisKey")
    public void addStadiumGeoKey() {
        List<TbStadium> tbStadiums = stadiumMapper.queryAll();
        for (TbStadium stadium : tbStadiums) {
            redisService.setGeo("location", Double.valueOf(stadium.getMapLongitude()), Double.valueOf(stadium.getMapLatitude()), stadium.getStadiumId().toString());
        }
    }

    //查询附近n米内的场馆
    @RequestMapping(value = "location")
    public List<GeoVo> queryGeo(double longitude, double latitude, String mi) {
        redisService.setGeo("location", Double.valueOf("114.087773"), Double.valueOf("35.110119"), "江波车行");
        redisService.setGeo("location", Double.valueOf("114.086816"), Double.valueOf("35.109838"), "惠民超市");
        System.out.println("距离" + redisService.getDistance("location", "江波车行", "惠民超时"));
        return redisKitService.getShopIdByGeo("location", longitude, latitude, mi);
    }

    @GetMapping("/redisTest")
    public void test() {
        /*User user = new User();
        user.setAge(18);
        user.setMobile("15903031938");
        user.setName("lzq");

        User user1 = new User();
        user1.setAge(20);
        user1.setMobile("15660855898");
        user1.setName("lzq1");
        redisUtils.lPush("list",user.toString());
        redisUtils.lPush("list",user1.toString());
        System.out.println("第一次打印："+redisUtils.lRange("list",0,-1));
        redisUtils.lRemove("list",0,user1.toString());
        user1.setName("lzq");
        user1.setMobile("15903031988");
        user1.setAge(24);
        redisUtils.lPush("list",user1.toString());
        System.out.println("第二次打印"+redisUtils.lRange("list",0,-1));*/
        redisUtils.lRemove("list",0,"wwww");
    }

    @RequestMapping(value = "/getRedisLock")
    public void testRedisLock(String name) throws InterruptedException {
        System.out.println(getLock(name));
    }

    @RedisLockAnnotation(typeEnum = RedisLockTypeEnum.TWO, lockTime = 3)
    public String getLock(String name) throws InterruptedException {
        try {
            logger.info("睡眠执行前");
            Thread.sleep(10000);
            logger.info("睡眠执行后");
        } catch (Exception e) {
            logger.info("has some error", e);
        }
        return "你好！欢迎来到RedisLock：" + name;
    }
}
