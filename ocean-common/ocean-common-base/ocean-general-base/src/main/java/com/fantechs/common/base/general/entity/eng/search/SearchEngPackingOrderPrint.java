package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
@Data
public class SearchEngPackingOrderPrint extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "despatchBatch",value = "发货批次")
    private String despatchBatch;
    @ApiModelProperty(name = "packingOrderCode",value = "装箱单号")
    private String packingOrderCode;
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;
    @ApiModelProperty(name = "contractCode",value = "合同号")
    private String contractCode;
    @ApiModelProperty(name = "cartonCode",value = "包装箱号")
    private String cartonCode;
    @ApiModelProperty(name = "innerCartonCode",value = "内箱号")
    private String innerCartonCode;
    @ApiModelProperty(name = "rawMaterialCode",value = "材料编码")
    private String rawMaterialCode;
    @ApiModelProperty(name = "deviceCode",value = "装置码")
    private String deviceCode;
    @ApiModelProperty(name = "locationNum",value = "位号")
    private String locationNum;
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(name = "partNumber",value = "件号")

    private String partNumber;

    @ApiModelProperty(name = "drawingNumber",value = "图号")
    private String drawingNumber;

    @ApiModelProperty(name = "shipmentEnterpriseName",value = "物流商")
    private String shipmentEnterpriseName;
}
