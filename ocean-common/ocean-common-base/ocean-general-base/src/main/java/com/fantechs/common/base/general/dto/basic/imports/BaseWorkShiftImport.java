package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseWorkShiftImport implements Serializable {

    /**
     * 班次编码
     */
    @ApiModelProperty(name="workShiftCode",value = "班次编码")
    @Excel(name = "班次编码(必填)", height = 20, width = 30)
    private String workShiftCode;

    /**
     * 班次名称
     */
    @ApiModelProperty(name="workShiftName",value = "班次名称")
    @Excel(name = "班次名称(必填)", height = 20, width = 30)
    private String workShiftName;

    /**
     * 班次描述
     */
    @ApiModelProperty(name="workShiftDesc",value = "班次描述")
    @Excel(name = "班次描述", height = 20, width = 30)
    private String workShiftDesc;

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
    private Byte status;

    /**
     * 开始时间
     */
    @ApiModelProperty(name="startTime",value = "开始时间")
    @Excel(name = "开始时间(必填)", height = 20, width = 30)
    @JsonFormat(pattern = "HH:mm")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="workShiftDesc",value = "结束时间")
    @Excel(name = "结束时间(必填)", height = 20, width = 30)
    @JsonFormat(pattern = "HH:mm")
    private Date endTime;
}
