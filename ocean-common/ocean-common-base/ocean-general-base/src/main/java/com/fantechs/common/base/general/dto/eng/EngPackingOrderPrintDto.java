package com.fantechs.common.base.general.dto.eng;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
@Data
public class EngPackingOrderPrintDto implements Serializable {
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
    @ApiModelProperty(name = "packingOrderId",value = "装箱清单id")
    private Long packingOrderId;
    @ApiModelProperty(name = "packingOrderSummaryId",value = "装箱汇总id")
    private Long packingOrderSummaryId;
    @ApiModelProperty(name = "packingOrderSummaryDetId",value = "装箱汇总明细id")
    private Long packingOrderSummaryDetId;
    @ApiModelProperty(name = "createUserName",value = "创建人")
    private String createUserName;
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private Data createTime;
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    private String modifiedUserName;
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    private Data modifiedTime;
}
