package com.fantechs.common.base.electronic.dto;

import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PtlJobOrderDto extends PtlJobOrder implements Serializable {

    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="vehicleCode",value = "集货号")
    private String vehicleCode;

    @ApiModelProperty(name="workerUser编码",value = "作业人员编码")
    private String workerUserCode;

    @ApiModelProperty(name = "createUserCode",value = "创建账号")
    private String createUserCode;

    @ApiModelProperty(name = "modifiedUserCode",value = "修改账号")
    private String modifiedUserCode;
}
