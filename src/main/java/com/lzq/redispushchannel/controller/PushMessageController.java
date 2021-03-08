package com.lzq.redispushchannel.controller;

import com.lzq.redispushchannel.config.redisLock.RedisLockAnnotation;
import com.lzq.redispushchannel.config.redisLock.RedisLockTypeEnum;
import com.lzq.redispushchannel.dto.*;
import com.lzq.redispushchannel.mapper.StadiumMapper;
import com.lzq.redispushchannel.service.NearbyBizService;
import com.lzq.redispushchannel.service.RedisKitService;
import com.lzq.redispushchannel.service.RedisService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Book;
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
    private static final Logger logger = LoggerFactory.getLogger(PushMessageController.class);
    @GetMapping("/pushMessage")
    public void pushMessage(String message){
        redisTemplate.convertAndSend("messagepush",message);
        redisTemplate.convertAndSend("messagepush1",message);
    }

    // 附近的人
    @RequestMapping(value = "nearby")
    public Result<List<NearbyBO>> nearby(@Valid NearbyPO paramObj) {
        return bizService.nearby(paramObj);
    }
    // 将商家存入到redis中
    @RequestMapping(value = "redisKey")
    public void addStadiumGeoKey(){
        List<TbStadium> tbStadiums = stadiumMapper.queryAll();
        for (TbStadium stadium: tbStadiums) {
            redisService.setGeo("location",Double.valueOf(stadium.getMapLongitude()),Double.valueOf(stadium.getMapLatitude()),stadium.getStadiumId().toString());
        }
    }
    //查询附近n米内的场馆
    @RequestMapping(value = "location")
    public List<GeoVo> queryGeo(double longitude, double latitude,String mi){
        redisService.setGeo("location",Double.valueOf("114.087773"),Double.valueOf("35.110119"),"江波车行");
        redisService.setGeo("location",Double.valueOf("114.086816"),Double.valueOf("35.109838"),"惠民超市");
        System.out.println("距离"+redisService.getDistance("location", "江波车行", "惠民超时"));
        return redisKitService.getShopIdByGeo("location",longitude,latitude,mi);
    }
    @Test
    public void test(){
        redisService.setGeo("location",Double.valueOf("114.087773"),Double.valueOf("35.110119"),"江波车行");
        redisService.setGeo("location",Double.valueOf("114.086816"),Double.valueOf("35.109838"),"惠民超市");
        System.out.println("距离"+redisService.getDistance("location", "江波车行", "惠民超时"));
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
        return "你好！欢迎来到RedisLock："+name;
    }
}
