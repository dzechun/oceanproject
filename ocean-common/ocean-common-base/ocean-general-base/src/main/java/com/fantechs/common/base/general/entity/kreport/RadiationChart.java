package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 辐射图
 */
@Data
public class RadiationChart extends ValidGroup implements Serializable {

    /**
     * 仓库地区名称
     */
    private String warehouseDistrictName;

    /**
     * 地区名称
     */
    @ApiModelProperty(name="addressName",value = "地区名称")
    private String districtName;

    /**
     * 经度
     */
    @ApiModelProperty(name="total",value = "发运总量")
    private String lng;

    /**
     * 纬度
     */
    @ApiModelProperty(name="carrierName",value = "承运商名称")
    private String lat;



}
