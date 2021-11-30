package com.fantechs.common.base.general.dto.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CallAgvWarehouseAreaMaterialDto {

    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="1")
    private String warehouseName;

    @ApiModelProperty(name="warehouseAreaName",value = "库区")
    @Excel(name = "库区", height = 20, width = 30,orderNum="2")
    private String warehouseAreaName;

    @ApiModelProperty(name="productModel",value = "物料型号")
    @Excel(name = "物料型号", height = 20, width = 30,orderNum="3")
    private String productModel;

    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="4")
    private BigDecimal qty;
}
