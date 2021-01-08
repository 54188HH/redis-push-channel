package com.lzq.redispushchannel.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/1/8 10:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyPO {
    @NotNull(message = "id值不能为空")
    private Integer id;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotNull(message = "城市编码不能为空")
    private Integer cityCode;
    @NotNull(message = "地区编码不能为空")
    private Integer adCode;
    @NotNull(message = "经度不能为空")
    private Double longitude;
    @NotNull(message = "纬度不能为空")
    private Double latitude;
}