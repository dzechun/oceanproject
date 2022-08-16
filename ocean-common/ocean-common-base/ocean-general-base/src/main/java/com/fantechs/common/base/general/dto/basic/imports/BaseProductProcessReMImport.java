package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseProductProcessReMImport implements Serializable {

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="productMaterialId",value = "产品料号ID")
    private Long productMaterialId;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="productMaterialCode",value = "产品料号")
    @Excel(name = "产品料号(必填)", height = 20, width = 30)
    private String productMaterialCode;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码(必填)", height = 20, width = 30)
    private String processCode;

    /**
     * 扫描类别(1-物料 2-条码)
     */
    @ApiModelProperty(name="scanType",value = "扫描类别(1-物料 2-条码)")
    @Excel(name = "扫描类别(1-物料 2-条码)", height = 20, width = 30)
    private Integer scanType;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 标签类别ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别ID")
    private Long labelCategoryId;

    /**
     * 标签类别编码
     */
    @ApiModelProperty(value = "labelCategoryCode",example = "标签类别编码")
    @Excel(name = "标签类别编码", height = 20, width = 30)
    private String labelCategoryCode;

    /**
     * 零件替代料ID
     */
    @ApiModelProperty(name="subMaterialId",value = "替代料ID")
    private Long subMaterialId;

    /**
     * 零件替代料编码
     */
    @ApiModelProperty(value = "subMaterialCode",example = "零件替代料编码")
    @Excel(name = "零件替代料编码", height = 20, width = 30)
    private String subMaterialCode;

    /**
     * 单个用量
     */
    @ApiModelProperty(name="usageQty",value = "单个用量")
    @Excel(name = "单个用量", height = 20, width = 30)
    private BigDecimal usageQty;
}
