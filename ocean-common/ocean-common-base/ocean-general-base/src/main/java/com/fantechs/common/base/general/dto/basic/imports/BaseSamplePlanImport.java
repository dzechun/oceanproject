package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseSamplePlanImport implements Serializable {

    /**
     * 抽样方案编码
     */
    @ApiModelProperty(name="samplePlanCode" ,value="抽样方案编码")
    @Excel(name = "抽样方案编码(必填)", height = 20, width = 30)
    private String samplePlanCode;

    /**
     * 抽样方案描述
     */
    @ApiModelProperty(name="samplePlanDesc" ,value="抽样方案描述")
    @Excel(name = "抽样方案描述", height = 20, width = 30)
    private String samplePlanDesc;

    /**
     * 检验水平
     */
    @ApiModelProperty(name="testLevel",value = "检验水平")
    @Excel(name = "检验水平", height = 20, width = 30)
    private String testLevel;

    /**
     * 抽样标准id
     */
    @ApiModelProperty(name="sampleStandardId",value = "抽样标准id")
    private Long sampleStandardId;

    /**
     * 抽样标准名称
     */
    @ApiModelProperty(name="sampleStandardName",value = "抽样标准名称")
    @Excel(name = "抽样标准名称", height = 20, width = 30)
    private String sampleStandardName;

    /**
     * 备注
     */
    @ApiModelProperty(name="status",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
