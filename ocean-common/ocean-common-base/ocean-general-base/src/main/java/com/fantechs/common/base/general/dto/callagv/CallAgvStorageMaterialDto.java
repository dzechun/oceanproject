package com.fantechs.common.base.general.dto.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CallAgvStorageMaterialDto {

    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="1")
    private String warehouseName;

    @ApiModelProperty(name="warehouseAreaName",value = "库区")
    @Excel(name = "库区", height = 20, width = 30,orderNum="2")
    private String warehouseAreaName;

    @ApiModelProperty(name="storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="3")
    private String storageCode;

    @ApiModelProperty(name="storageType",value = "库位类型（1-存货 2-收货 3-发货）")
    @Excel(name = " 库位类型（1-存货 2-收货 3-发货）", height = 20, width = 30,orderNum="4",replace = {"存货_1","收货_2", "发货_3"})
    private Byte storageType;

    @ApiModelProperty(name="vehicleCode",value = "货架")
    @Excel(name = "货架", height = 20, width = 30,orderNum="5")
    private String vehicleCode;

    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="6")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "物料型号")
    @Excel(name = "物料型号", height = 20, width = 30,orderNum="7")
    private String productModel;

    @ApiModelProperty(name="erpProductModel",value = "erp物料型号")
    @Excel(name = "erp物料型号", height = 20, width = 30,orderNum="8")
    private String erpProductModel;

    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="9")
    private BigDecimal qty;

    @ApiModelProperty(name="batch",value = "批次")
    @Excel(name = "批次", height = 20, width = 30,orderNum="10")
    private String batch;

    @ApiModelProperty(name="batchCode",value = "批号")
    @Excel(name = "批号", height = 20, width = 30,orderNum="11")
    private String batchCode;
}
