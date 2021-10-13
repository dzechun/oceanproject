package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PtlJobOrderDto extends PtlJobOrder implements Serializable {

    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="vehicleCode",value = "集货号")
    @Excel(name = "集货号", height = 20, width = 30,orderNum="3")
    private String vehicleCode;

    @ApiModelProperty(name="workerUser编码",value = "作业人员编码")
    private String workerUserCode;

    @ApiModelProperty(name = "createUserCode",value = "创建账号")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="8")
    private String createUserCode;

    @ApiModelProperty(name = "modifiedUserCode",value = "修改账号")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="12")
    private String modifiedUserCode;

    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域id")
    private Long warehouseAreaId;

    @ApiModelProperty(name = "warehouseAreaCode",value = "仓库区域编码")
    private String warehouseAreaCode;

    @ApiModelProperty(name = "warehouseAreaName",value = "仓库区域名称")
    private String warehouseAreaName;

    @ApiModelProperty(name = "seq",value = "任务单激活顺序")
    private Integer seq;

    @ApiModelProperty(name = "isNew",value = "是否是新激活任务（0-不是 1-是）")
    private Integer isNew;
}
