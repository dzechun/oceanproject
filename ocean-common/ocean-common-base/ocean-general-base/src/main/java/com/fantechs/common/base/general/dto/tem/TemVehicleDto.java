package com.fantechs.common.base.general.dto.tem;

import com.fantechs.common.base.general.entity.tem.TemVehicle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TemVehicleDto extends TemVehicle implements Serializable {

    @ApiModelProperty(name = "createUserCode",value = "创建账号")
    private String createUserCode;

    @ApiModelProperty(name = "modifiedUserCode",value = "修改账号")
    private String modifiedUserCode;

    @ApiModelProperty(name = "count",value = "使用次数")
    private Long count;
}
