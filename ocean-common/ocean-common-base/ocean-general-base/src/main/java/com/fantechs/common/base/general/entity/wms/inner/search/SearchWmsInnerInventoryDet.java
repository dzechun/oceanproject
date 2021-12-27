package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class SearchWmsInnerInventoryDet extends BaseQuery implements Serializable {

    @ApiModelProperty("库位id")
    private Long storageId;
    @ApiModelProperty("相关单号")
    private String relevanceOrderCode;
    @ApiModelProperty("是否是不相等 -- 0、相等 1、不相等")
    private Integer notEqualMark;

    private String warehouseName;

    private String asnCode;

    private Byte ifStockLock;

    private String createUserName;

    private String modifiedUserName;

    @ApiModelProperty("库位")
    private String storageCode;

    @ApiModelProperty("库存状态id")
    private Long inventoryStatusId;

    @ApiModelProperty("检验单编码")
    private String inspectionOrderCode;

    @ApiModelProperty("检验单编码是否为空  0-否 1-是")
    private Integer ifInspectionOrderCodeNull;

    @ApiModelProperty(name = "deliveryOrderCode",value = "发货单号")
    private String deliveryOrderCode;

    @ApiModelProperty(name = "barcodeStatus",value = "条码状态")
    private String barcodeStatus;

    @ApiModelProperty(name="qcDate",value = "质检日期")
    private String qcDate;

    @ApiModelProperty("SN码")
    private String barcode;

    @ApiModelProperty("彩盒码")
    private String colorBoxCode;

    @ApiModelProperty("箱码")
    private String cartonCode;

    @ApiModelProperty("栈板码")
    private String palletCode;
}
