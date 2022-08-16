package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseBatchRulesImport implements Serializable {

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    @Excel(name = "仓库编码(必填)", height = 20, width = 30)
    private String warehouseCode;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    private Long warehouseId;

    /**
     * 货主编码
     */
    @ApiModelProperty(name="materialOwnerCode" ,value="货主编码")
    @Excel(name = "货主编码(必填)", height = 20, width = 30)
    private String materialOwnerCode;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    private Long materialOwnerId;

    /**
     * 批次规则名称
     */
    @ApiModelProperty(name="batchRulesName",value = "批次规则名称")
    @Excel(name = "批次规则名称(必填)", height = 20, width = 30)
    private String batchRulesName;

    /**
     * 不混货品(0-否 1-是)
     */
    @ApiModelProperty(name="notMixedWith",value = "不混货品(0-否 1-是)")
    @Excel(name = "不混货品(0-否 1-是)", height = 20, width = 30)
    private Integer notMixedWith;

    /**
     * 跟踪批次号(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterBatch",value = "跟踪批次号(0-否 1-是)")
    @Excel(name = "跟踪批次号(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterBatch;

    /**
     * 跟踪生产日期(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterDateInProduced",value = "跟踪生产日期(0-否 1-是)")
    @Excel(name = "跟踪生产日期(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterDateInProduced;

    /**
     * 跟踪收货单号(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterReceivingCode",value = "跟踪收货单号(0-否 1-是)")
    @Excel(name = "跟踪收货单号(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterReceivingCode;

    /**
     * 跟踪质检日期(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterQualityDate",value = "跟踪质检日期(0-否 1-是)")
    @Excel(name = "跟踪质检日期(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterQualityDate;

    /**
     * 跟踪销售单(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterSaleCode",value = "跟踪销售单(0-否 1-是)")
    @Excel(name = "跟踪销售单(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterSaleCode;

    /**
     * 跟踪供应商(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterSupplier",value = "跟踪供应商(0-否 1-是)")
    @Excel(name = "跟踪供应商(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterSupplier;

    /**
     * 跟踪收货日期(0-否 1-是)
     */
    @ApiModelProperty(name="tailAfterReceiveDate",value = "跟踪收货日期(0-否 1-是)")
    @Excel(name = "跟踪收货日期(0-否 1-是)", height = 20, width = 30)
    private Integer tailAfterReceiveDate;
}
