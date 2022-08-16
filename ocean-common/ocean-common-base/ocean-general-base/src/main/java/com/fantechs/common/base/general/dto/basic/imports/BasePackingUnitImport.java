package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasePackingUnitImport implements Serializable {

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称(必填)", height = 20, width = 30)
    private String packingUnitName;

    /**
     * 包装单位描述
     */
    @ApiModelProperty(name="packingUnitDesc",value = "包装单位描述")
    @Excel(name = "包装单位描述", height = 20, width = 30)
    private String packingUnitDesc;

    /**
     * 是否主要(0否，1是)
     */
    @ApiModelProperty(name="isChief",value = "是否主要(0否，1是)")
    @Excel(name = "是否主要(0否，1是)", height = 20, width = 30)
    private Integer isChief;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
