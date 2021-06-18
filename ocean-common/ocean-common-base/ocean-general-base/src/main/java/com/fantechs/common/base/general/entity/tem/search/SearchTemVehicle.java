package com.fantechs.common.base.general.entity.tem.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchTemVehicle extends BaseQuery implements Serializable {

    @ApiModelProperty(name="vehicleCode",value = "周转工具编码")
    private String vehicleCode;

    @ApiModelProperty(name="vehicleName",value = "周转工具名称")
    private String vehicleName;

    @ApiModelProperty(name="vehicleStatus",value = "周转工具状态(1-空闲 2-出库中 3-使用中 4-入库中)")
    private Byte vehicleStatus;
}