package com.lzq.redispushchannel.controller;

import com.lzq.redispushchannel.dto.*;
import com.lzq.redispushchannel.mapper.StadiumMapper;
import com.lzq.redispushchannel.service.NearbyBizService;
import com.lzq.redispushchannel.service.RedisKitService;
import com.lzq.redispushchannel.service.RedisService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
       return redisKitService.getShopIdByGeo("location",longitude,5,mi);
    }
    @Test
    public void test(){
        System.out.println(redisService.boundGeoOps("location"));
    }
}
