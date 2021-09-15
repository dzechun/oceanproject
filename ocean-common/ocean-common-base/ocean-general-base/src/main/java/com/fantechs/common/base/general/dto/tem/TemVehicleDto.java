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

    @ApiModelProperty(name="taskPointCode",value = "配送点编码")
    private String taskPointCode;

    @ApiModelProperty(name="taskPointName",value = "配送点名称")
    private String taskPointName;

    @ApiModelProperty(name="xyzCode",value = "坐标编码")
    private String xyzCode;

    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @ApiModelProperty(name="warehouseAreaCode" ,value="库区编码")
    private String warehouseAreaCode;

    @ApiModelProperty(name="workingAreaCode" ,value="工作区编码")
    private String workingAreaCode;

    @ApiModelProperty(name="workingAreaName" ,value="工作区名称")
    private String workingAreaName;

    @ApiModelProperty(name="storageCode" ,value="库位编码")
    private String storageCode;

    @ApiModelProperty(name="storageName" ,value="库位名称")
    private String storageName;
}
