package com.fantechs.common.base.general.dto.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.callagv.CallAgvProductionInLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CallAgvProductionInLogDto extends CallAgvProductionInLog {

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

    @ApiModelProperty(name="model",value = "物料型号")
    @Excel(name = "物料型号", height = 20, width = 30,orderNum="9")
    private String model;

    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="10")
    private BigDecimal qty;

    @ApiModelProperty(name="batchCode",value = "生产批号")
    @Excel(name = "生产批号", height = 20, width = 30,orderNum="11")
    private String batchCode;
}
