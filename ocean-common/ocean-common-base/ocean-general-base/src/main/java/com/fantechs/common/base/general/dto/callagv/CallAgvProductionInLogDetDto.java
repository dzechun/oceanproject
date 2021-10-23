package com.fantechs.common.base.general.dto.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CallAgvProductionInLogDetDto {

    @ApiModelProperty(name="inWarehouseName",value = "入库仓库")
    @Excel(name = "入库仓库", height = 20, width = 30,orderNum="1")
    private String inWarehouseName;

    @ApiModelProperty(name="outWarehouseName",value = "出库仓库")
    @Excel(name = "出库仓库", height = 20, width = 30,orderNum="2")
    private String outWarehouseName;

    @ApiModelProperty(name="inWarehouseAreaName",value = "入库库区")
    @Excel(name = "入库库区", height = 20, width = 30,orderNum="3")
    private String inWarehouseAreaName;

    @ApiModelProperty(name="outWarehouseAreaName",value = "出库库区")
    @Excel(name = "出库库区", height = 20, width = 30,orderNum="4")
    private String outWarehouseAreaName;

    @ApiModelProperty(name="inStorageCode",value = "入库库位")
    @Excel(name = "入库库位", height = 20, width = 30,orderNum="5")
    private String inStorageCode;

    @ApiModelProperty(name="outStorageCode",value = "出库库位")
    @Excel(name = "出库库位", height = 20, width = 30,orderNum="6")
    private String outStorageCode;

    @ApiModelProperty(name="inTaskPointName",value = "入库配送点")
    @Excel(name = "入库配送点", height = 20, width = 30,orderNum="7")
    private String inTaskPointName;

    @ApiModelProperty(name="outTaskPointName",value = "出库配送点")
    @Excel(name = "出库配送点", height = 20, width = 30,orderNum="8")
    private String outTaskPointName;

    @ApiModelProperty(name="barcode",value = "物料条码")
    @Excel(name = "物料条码", height = 20, width = 30,orderNum="9")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "物料型号")
    @Excel(name = "物料型号", height = 20, width = 30,orderNum="10")
    private String productModel;

    @ApiModelProperty(name="qty",value = "出入库数量")
    @Excel(name = "出入库数量", height = 20, width = 30,orderNum="11")
    private BigDecimal qty;

    @ApiModelProperty(name="batchCode",value = "生产批号")
    @Excel(name = "生产批号", height = 20, width = 30,orderNum="12")
    private String batchCode;

    @ApiModelProperty(name="operateTime",value = "操作时间")
    @Excel(name = "操作时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date operateTime;
}
