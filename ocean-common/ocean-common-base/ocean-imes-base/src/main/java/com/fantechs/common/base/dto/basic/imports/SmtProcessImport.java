package com.fantechs.common.base.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SmtProcessImport implements Serializable {

    /**
     * 工序代码
     */
    @ApiModelProperty(name="processCode" ,value="工序代码")
    @Excel(name = "工序代码", height = 20, width = 30)
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    private String processName;

    /**
     * 工序描述
     */
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    @Excel(name = "工序描述", height = 20, width = 30)
    private String processDesc;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    private Long sectionId;

    /**
     * 工段编码
     */
    @ApiModelProperty(name="sectionCode" ,value="工段编码")
    @Excel(name = "工段编码", height = 20, width = 30)
    private String sectionCode;

    /**
     * 工序类别ID
     */
    @ApiModelProperty(name="processCategoryId" ,value="工序类别ID")
    private Long processCategoryId;

    /**
     * 工序类别编码
     */
    @ApiModelProperty(name="processCategoryCode" ,value="工序类别编码")
    @Excel(name = "工序类别编码", height = 20, width = 30)
    private String processCategoryCode;

    /**
     * 是否报工扫描（0、否 1、是）
     */
    @ApiModelProperty(name="isJobScan" ,value="是否报工扫描")
    @Excel(name = "是否报工扫描", height = 20, width = 30)
    private Byte isJobScan;

    /**
     * 是否开工扫描（0、否 1、是）
     */
    @ApiModelProperty(name= "isStartScan" ,value="是否开工扫描")
    @Excel(name = "是否开工扫描", height = 20, width = 30)
    private Byte isStartScan;

    /**
     * 是否品质确认（0、否 1、是）
     */
    @ApiModelProperty(name="isQuality" ,value="是否品质确认")
    @Excel(name = "是否品质确认", height = 20, width = 30)
    private Byte isQuality;

    /**
     * 完成时间
     */
    @Excel(name = "完成时间",  height = 20, width = 30)
    @ApiModelProperty(name="finishTime" ,value="完成时间")
    private BigDecimal finishTime;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Integer status;
}
