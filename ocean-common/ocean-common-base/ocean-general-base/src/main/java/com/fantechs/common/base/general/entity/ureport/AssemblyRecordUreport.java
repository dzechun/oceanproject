package com.fantechs.common.base.general.entity.ureport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AssemblyRecordUreport extends ValidGroup implements Serializable {

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    private String processName;

    /**
     * 物料类别(1-物料 2-条码)
     */
    @ApiModelProperty(name="scanType",value = "物料类别(1-物料 2-条码)")
    private Byte scanType;

    /**
     * 零件料号
     */
    @ApiModelProperty(name="materialCode",value = "零件料号")
    private String materialCode;

    /**
     * 零件料号版本
     */
    @ApiModelProperty(name="materialVer",value = "零件料号版本")
    private String materialVer;

    /**
     * 零件料号描述
     */
    @ApiModelProperty(name="materialDesc",value = "零件料号描述")
    private String materialDesc;

    /**
     * 条码名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "条码名称")
    private String labelCategoryName;

    /**
     * 用量
     */
    @ApiModelProperty(name="dosage",value = "用量")
    private BigDecimal dosage;

}
