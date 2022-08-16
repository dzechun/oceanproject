package com.fantechs.common.base.general.dto.wms.inner.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class InitStockImport implements Serializable {
    /**
     * 初始化盘点单号
     */
    @ApiModelProperty(name="initStockOrderCode",value = "初始化盘点单号")
    //@Excel(name = "初始化盘点单号", height = 20, width = 30)
    private String initStockOrderCode;

    @ApiModelProperty(name = "storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30)
    private String storageCode;

    private Long storageId;

    /**
     * 盘点类型：1-初始化盘点，2-正常盘点
     */
    @ApiModelProperty(name="initStockType",value = "盘点类型：1-初始化盘点，2-正常盘点")
    @Excel(name = "盘点类型", height = 20, width = 30)
    private String initStockType;


//    ====================盘点明细=====================

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    private Long materialId;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30)
    private BigDecimal planQty;
}
