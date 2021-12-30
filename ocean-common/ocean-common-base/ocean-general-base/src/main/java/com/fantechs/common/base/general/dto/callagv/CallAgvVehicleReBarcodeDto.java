package com.fantechs.common.base.general.dto.callagv;

import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CallAgvVehicleReBarcodeDto extends CallAgvVehicleReBarcode implements Serializable {

    @ApiModelProperty(name="vehicleCode",value = "周转工具编码")
    private String vehicleCode;

    @ApiModelProperty(name="vehicleName",value = "周转工具名称")
    private String vehicleName;

    @ApiModelProperty(name="taskPointCode",value = "配送点编码")
    private String taskPointCode;

    @ApiModelProperty(name="taskPointName",value = "配送点名称")
    private String taskPointName;

    @ApiModelProperty(name="xyzCode",value = "坐标编码")
    private String xyzCode;

    @ApiModelProperty(name="storageCode" ,value="库位编码")
    private String storageCode;

    @ApiModelProperty(name="storageName" ,value="库位名称")
    private String storageName;

    @ApiModelProperty(name="batch",value = "批次")
    private String batch;

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "型号")
    private String productModel;

    @ApiModelProperty(name="erpProductModel",value = "erp物料型号")
    private String erpProductModel;

    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(name="materialFactory",value = "厂家")
    private String materialFactory;

    @ApiModelProperty(name="barcodeStatus",value = "条码状态(1-待入库 2-已备料 3-已入库 4-已出库 5-已解绑)")
    private Byte barcodeStatus;
}
