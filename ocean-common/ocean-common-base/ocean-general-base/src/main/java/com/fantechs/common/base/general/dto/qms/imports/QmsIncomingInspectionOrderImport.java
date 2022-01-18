package com.fantechs.common.base.general.dto.qms.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class QmsIncomingInspectionOrderImport implements Serializable {

    /**
     * 组号(必填)
     */
    @ApiModelProperty(name="groupCode",value = "组号(必填)")
    @Excel(name = "组号(必填)", height = 20, width = 30)
    private String groupCode;

    /**
     * 产品料号(必填)
     */
    @Excel(name = "产品料号(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="产品料号(必填)")
    private String materialCode;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId" ,value="产品ID")
    private Long materialId;

    /**
     * 供应商编码
     */
    @Excel(name = "供应商编码", height = 20, width = 30)
    @ApiModelProperty(name="supplierCode" ,value="供应商编码")
    private String supplierCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId" ,value="供应商ID")
    private Long supplierId;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", height = 20, width = 30)
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    private Long warehouseId;

    /**
     * 单据数量
     */
    @ApiModelProperty(name="orderQty",value = "单据数量")
    @Excel(name = "单据数量", height = 20, width = 30)
    private BigDecimal orderQty;

    /**
     * 检验方式编码
     */
    @Excel(name = "检验方式编码", height = 20, width = 30)
    @ApiModelProperty(name="inspectionWayCode" ,value="检验方式编码")
    private String inspectionWayCode;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId" ,value="检验方式ID")
    private Long inspectionWayId;

    /**
     * 检验标准编码
     */
    @Excel(name = "检验标准编码", height = 20, width = 30)
    @ApiModelProperty(name="inspectionStandardCode" ,value="检验标准编码")
    private String inspectionStandardCode;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId" ,value="检验标准ID")
    private Long inspectionStandardId;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    @Excel(name = "检验状态(1-待检验 2-检验中 3-已检验)", height = 20, width = 30)
    private Integer inspectionStatus;


    //明细---------------------------

    /**
     * 是否必检(0-否 1-是)
     */
    @ApiModelProperty(name="ifMustInspection",value = "是否必检(0-否 1-是)")
    @Excel(name = "是否必检(0-否 1-是)", height = 20, width = 30)
    private Integer ifMustInspection;

    /**
     * 检验项目编码-大类
     */
    @ApiModelProperty(name="bigInspectionItemDesc" ,value="检验项目编码-大类")
    @Excel(name = "检验项目编码-大类", height = 20, width = 30)
    private String bigInspectionItemDesc;

    /**
     * 检验项目编码-小类
     */
    @ApiModelProperty(name="smallInspectionItemDesc" ,value="检验项目编码-小类")
    @Excel(name = "检验项目编码-小类", height = 20, width = 30)
    private String smallInspectionItemDesc;

    /**
     * 检验项目标准名称
     */
    @ApiModelProperty(name="inspectionStandardName",value = "检验项目标准名称")
    @Excel(name = "检验项目标准名称", height = 20, width = 30)
    private String inspectionStandardName;

    /**
     * 检验标识(1-定性 2-定量)
     */
    @ApiModelProperty(name="inspectionTag",value = "检验标识(1-定性 2-定量)")
    @Excel(name = "检验标识(1-定性 2-定量)", height = 20, width = 30)
    private Integer inspectionTag;

    /**
     * 样本数
     */
    @ApiModelProperty(name="sampleQty",value = "样本数")
    @Excel(name = "样本数", height = 20, width = 30)
    private BigDecimal sampleQty;

    /**
     * 规格上限
     */
    @ApiModelProperty(name="specificationUpperLimit",value = "规格上限")
    @Excel(name = "规格上限", height = 20, width = 30)
    private BigDecimal specificationUpperLimit;

    /**
     * 规格下限
     */
    @ApiModelProperty(name="specificationFloor",value = "规格下限")
    @Excel(name = "规格下限", height = 20, width = 30)
    private BigDecimal specificationFloor;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30)
    private String unitName;

    /**
     * AQL值
     */
    @ApiModelProperty(name="aqlValue",value = "AQL值")
    @Excel(name = "AQL值", height = 20, width = 30)
    private BigDecimal aqlValue;

    /**
     * AC值
     */
    @ApiModelProperty(name="acValue",value = "AC值")
    @Excel(name = "AC值", height = 20, width = 30)
    private Integer acValue;

    /**
     * RE值
     */
    @ApiModelProperty(name="reValue",value = "RE值")
    @Excel(name = "RE值", height = 20, width = 30)
    private Integer reValue;

}
