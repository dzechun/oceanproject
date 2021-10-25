package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCallAgvProductionInLog extends BaseQuery implements Serializable {

    @ApiModelProperty(name="inWarehouseName",value = "入库仓库")
    private String inWarehouseName;

    @ApiModelProperty(name="outWarehouseName",value = "出库仓库")
    private String outWarehouseName;

    @ApiModelProperty(name="inWarehouseAreaName",value = "入库库区")
    private String inWarehouseAreaName;

    @ApiModelProperty(name="outWarehouseAreaName",value = "出库库区")
    private String outWarehouseAreaName;

    @ApiModelProperty(name="inStorageCode",value = "入库库位")
    private String inStorageCode;

    @ApiModelProperty(name="outStorageCode",value = "出库库位")
    private String outStorageCode;

    @ApiModelProperty(name="inTaskPointName",value = "入库配送点")
    private String inTaskPointName;

    @ApiModelProperty(name="outTaskPointName",value = "出库配送点")
    private String outTaskPointName;

    @ApiModelProperty(name="productModel",value = "物料型号")
    private String productModel;

    @ApiModelProperty(name="model",value = "物料型号汇总")
    private String model;

    @ApiModelProperty(name="ifGroupBy",value = "是否分组")
    private Integer ifGroupBy;
}
