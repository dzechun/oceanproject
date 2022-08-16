package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BaseBadnessCauseImport implements Serializable {

    /**
     * 不良原因编码(必填)
     */
    @ApiModelProperty(name="badnessCauseCode",value = "不良原因编码(必填)")
    @Excel(name = "不良原因编码(必填)", height = 20, width = 30)
    private String badnessCauseCode;

    /**
     * 不良原因描述
     */
    @ApiModelProperty(name="badnessCauseDesc",value = "不良原因描述")
    @Excel(name = "不良原因描述", height = 20, width = 30)
    private String badnessCauseDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
