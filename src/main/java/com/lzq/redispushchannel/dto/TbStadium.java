package com.lzq.redispushchannel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TbStadium {
    /**
     * 主键id
     */
    private Integer stadiumId;
    /**
     * 场馆名称
     */
    private String stadiumName;
    /**
     * 场馆简称
     */
    private String stadiumAbb;

    /**
     * 场馆区域（江干区）
     */
    private String stadiumArea;

    /**
     * 场馆具体地址
     */
    private String stadiumAddress;

    /**
     * 地图经度 121.533113
     */
    private String mapLongitude;

    /**
     * 地图纬度 31.182301
     */
    private String mapLatitude;

    /**
     * 场馆缩略图 图片
     */
    private String stadiumImage;

    /**
     * 场馆详情页面图片
     */
    private String stadiumDetailimage;

    /**
     * 标签值id,逗号分隔
     */
    private String tagsId;

    /**
     * 标签值,逗号分隔
     */
    private String tags;

    /**
     * 场馆营业时间段
     */
    private String stadiumOpentime;

    /**
     * 场馆联系人电话
     */
    private String stadiumPhone;

    /**
     * 是否推荐0 不推荐  1推荐
     */
    private Integer stadiumRecommend;

    /**
     * 开放类型0未开放  1已开放
     */
    private Integer openType;

    /**
     * 1未删除，2已删除）
     */
    private Integer logicDelete;

    /**
     * 场馆项目（游泳,保龄球）
     */
    private String stadiumItems;

    /**
     * 场馆项目id逗号分隔（游泳,保龄球）
     */
    private String stadiumItemsId;

    /**
     * 集团Id
     */
    private Integer blocId;

    /**
     * 未开放票的图片地址
     */
    private String noOpenUrl;

    private Integer sort;

    private String appId;

    private String weiMerId;


    private String weiSignKey;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 支付宝的appid
     */
    private String alipayId;

    /**
     * 支付宝商户号
     */
    private String alipayAppPrivateKey;

    /**
     * 支付宝密钥
     */
    private String alipayPublicKey;
}