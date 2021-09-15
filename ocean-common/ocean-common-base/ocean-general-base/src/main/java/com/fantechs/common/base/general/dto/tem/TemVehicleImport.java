package com.fantechs.common.base.general.dto.tem;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TemVehicleImport implements Serializable {

    @ApiModelProperty(name="vehicleCode",value = "周转工具编码")
    @Excel(name = "周转工具编码(必填)", height = 20, width = 30)
    private String vehicleCode;

    @ApiModelProperty(name="vehicleName",value = "周转工具名称")
    @Excel(name = "周转工具名称(必填)", height = 20, width = 30)
    private String vehicleName;

    @ApiModelProperty(name="vehicleStatus",value = "周转工具状态(1-空闲 2-出库中 3-使用中 4-入库中)")
    @Excel(name = "周转工具状态(1-空闲 2-出库中 3-使用中 4-入库中)", height = 20, width = 30)
    private Integer vehicleStatus;

    @ApiModelProperty(name="standardCapacity",value = "标准装载容量")
    @Excel(name = "标准装载容量(必填)", height = 20, width = 30)
    private BigDecimal standardCapacity;

    @ApiModelProperty(name="agvTaskTemplate",value = "AGV任务模板")
    @Excel(name = "AGV任务模板(必填)", height = 20, width = 30)
    private String agvTaskTemplate;

    @ApiModelProperty(name = "createUserCode",value = "创建账号")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserCode;

    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "modifiedUserCode",value = "修改账号")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserCode;

    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
