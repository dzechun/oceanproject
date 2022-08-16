package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseWorkingAreaReW;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BaseWorkingAreaReWDto extends BaseWorkingAreaReW implements Serializable {
    /**
     * 工作区域编码
     */
    @ApiModelProperty(name="workingAreaCode",value = "工作区域编码")
    @Excel(name = "工作区域编码", height = 20, width = 30,orderNum="1")
    private String workingAreaCode;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name="warehouseAreaId" ,value="仓库区域ID")
    private Long warehouseAreaId;
    /**
     * 库区名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName",value = "库区名称")
    @Excel(name = "库区名称", height = 20, width = 30,orderNum="2")
    private String warehouseAreaName;
}
