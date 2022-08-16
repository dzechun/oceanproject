package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BasePackageSpecificationImport implements Serializable {

    /**
     * 包装规格编码
     */
    @ApiModelProperty(name="packageSpecificationCode",value = "包装规格编码")
    @Excel(name = "包装规格编码(必传)", height = 20, width = 30)
    private String packageSpecificationCode;

    /**
     * 包装规格名称
     */
    @ApiModelProperty(name="packageSpecificationName",value = "包装规格名称")
    @Excel(name = "包装规格名称(必传)", height = 20, width = 30)
    private String packageSpecificationName;

    /**
     * 包装规格描述
     */
    @ApiModelProperty(name="packageSpecificationDesc",value = "包装规格描述")
    @Excel(name = "包装规格描述", height = 20, width = 30)
    private String packageSpecificationDesc;

    /**
     * 包装规格数量
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格数量")
    @Excel(name = "包装规格数量", height = 20, width = 30)
    private BigDecimal packageSpecificationQuantity;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码(必传)", height = 20, width = 30)
    private String materialCode;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码(必传)", height = 20, width = 30)
    private String processCode;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    private Long barcodeRuleId;

    /**
     * 条码规则代码
     */
    @ApiModelProperty(name="barcodeRuleCode",value = "条码规则代码")
    @Excel(name = "条码规则代码", height = 20, width = 30)
    private String barcodeRuleCode;

    /**
     * 包装单位ID
     */
    @ApiModelProperty(name="packingUnitId",value = "包装单位ID")
    private Long packingUnitId;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称", height = 20, width = 30)
    private String packingUnitName;

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
