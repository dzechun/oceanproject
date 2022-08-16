package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseInspectionStandardImport implements Serializable {

    /**
     * 检验标准编码(必填)
     */
    @ApiModelProperty(name = "inspectionStandardCode",value = "检验标准编码(必填)")
    @Excel(name = "检验标准编码(必填)", height = 20, width = 30)
    private String inspectionStandardCode;

    /**
     * 检验标准名称(必填)
     */
    @ApiModelProperty(name = "inspectionStandardName",value = "检验标准名称(必填)")
    @Excel(name = "检验标准名称(必填)", height = 20, width = 30)
    private String inspectionStandardName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 客户编码
     */
    @ApiModelProperty(name="supplierCode",value = "客户编码")
    @Excel(name = "客户编码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    private Long supplierId;

    /**
     * 检验类型(1-来料检验 2-驻厂检验 3-出货检验 4-IPQC检验)
     */
    @ApiModelProperty(name="inspectionType",value = "检验类型(1-来料检验 2-驻厂检验 3-出货检验 4-IPQC检验)")
    @Excel(name = "检验类型(1-来料检验 2-驻厂检验 3-出货检验 4-IPQC检验)", height = 20, width = 30)
    private Integer inspectionType;

    /**
     * 检验方式编码
     */
    @ApiModelProperty(name="inspectionWayCode",value = "检验方式编码")
    @Excel(name = "检验方式编码", height = 20, width = 30)
    private String inspectionWayCode;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    private Long inspectionWayId;

    /**
     * 检验标准版本
     */
    @ApiModelProperty(name="inspectionStandardVersion",value = "检验标准版本")
    @Excel(name = "检验标准版本", height = 20, width = 30)
    private String inspectionStandardVersion;


    //--------------检验标准明细--------------

    /**
     * 是否必检(0-否 1-是)
     */
    @ApiModelProperty(name="ifMustInspection",value = "是否必检(0-否 1-是)")
    @Excel(name = "是否必检(0-否 1-是)", height = 20, width = 30)
    private Integer ifMustInspection;

    /**
     * 检验项目编码-大类
     */
    @ApiModelProperty(name="inspectionItemCodeBig" ,value="检验项目编码-大类")
    @Excel(name = "检验项目编码-大类", height = 20, width = 30)
    private String inspectionItemCodeBig;

    /**
     * 检验项目编码-小类
     */
    @ApiModelProperty(name="inspectionItemCodeSmall" ,value="检验项目编码-小类")
    @Excel(name = "检验项目编码-小类", height = 20, width = 30)
    private String inspectionItemCodeSmall;

    /**
     * 检验项目id
     */
    @ApiModelProperty(name="inspectionItemId" ,value="检验项目id")
    private Long inspectionItemId;

    /**
     * 检验工具
     */
    @ApiModelProperty(name="inspectionTool",value = "检验工具")
    @Excel(name = "检验工具", height = 20, width = 30)
    private String inspectionTool;

    /**
     * 抽样过程编码
     */
    @ApiModelProperty(name="sampleProcessCode" ,value="抽样过程编码")
    @Excel(name = "抽样过程编码", height = 20, width = 30)
    private String sampleProcessCode;

    /**
     * 抽样过程ID
     */
    @ApiModelProperty(name="sampleProcessId",value = "抽样过程ID")
    private Long sampleProcessId;

    /**
     * 检验标识(1-定性 2-定量)
     */
    @ApiModelProperty(name="inspectionTag",value = "检验标识(1-定性 2-定量)")
    @Excel(name = "检验标识(1-定性 2-定量)", height = 20, width = 30)
    private Integer inspectionTag;

}
