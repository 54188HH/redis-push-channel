package com.lzq.redispushchannel.service;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/8 11:06
 */

import com.lzq.redispushchannel.dto.GeoVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisKitService {

    @Autowired
    private RedisService redisService;

    /**
     * 根据给定地理位置坐标获取指定范围内的地理位置集合
     * @param key  键
     * @param longitude  经度   String类型
     * @param latitude   纬度   String类型
     * @param mi  距离  没有时默认3km
     * @return list  商家id集合
     */
    public List<GeoVo> getShopIdByGeo(String key, double longitude, double latitude, String mi){
        //不选择时，默认3000m
        if (mi==null){
            mi="3000";
        }
        double distance = Double.valueOf(mi);

        List<GeoVo> geos = new ArrayList<>();

        BoundGeoOperations boundGeoOperations = redisService.boundGeoOps("location");


        Point point = new Point(longitude, latitude);

        Circle within = new Circle(point, distance);

        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();

        //返回的结果包括 距离 和 坐标
        geoRadiusArgs = geoRadiusArgs.includeDistance();

        //限制查询返回的数量
        // geoRadiusArgs.limit(10);

        //按查询出的坐标距离中心坐标的距离进行排序
        geoRadiusArgs.sortAscending();

        GeoResults<GeoLocation<String>> geoResults = boundGeoOperations.radius(within, geoRadiusArgs);

        List<GeoResult<GeoLocation<String>>> geoResultList = geoResults.getContent();
        //GeoVo自己由于某些需求而做的一个封装类，可以直接用geoResultList
        GeoVo geoVo = null;
        for (GeoResult<GeoLocation<String>> geoResult : geoResultList) {
            geoVo = new GeoVo();
            String name = geoResult.getContent().getName();
            Distance distance1 = geoResult.getDistance();
            geoVo.setNumber(name);
            String str = String.valueOf(distance1);
            String m1 = StringUtils.substringBeforeLast(str, "M");
            geoVo.setDistance(m1);
            geos.add(geoVo);
        }
        return geos;
    }
}
