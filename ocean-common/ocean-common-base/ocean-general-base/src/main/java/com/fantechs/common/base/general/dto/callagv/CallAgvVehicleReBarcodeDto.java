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

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "型号")
    private String productModel;

    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;
}
