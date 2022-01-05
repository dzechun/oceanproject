package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class SearchWmsInnerInventoryDet extends BaseQuery implements Serializable {

    @ApiModelProperty("库位id")
    private Long storageId;
    @ApiModelProperty("物料ID")
    private Long materialId;
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

    @ApiModelProperty("条码id")
    private Long materialBarcodeId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("批次号")
    private String batchCode;

    /**
     * 生产日期开始时间
     */
    @ApiModelProperty(name="endTime" ,value="生产日期开始时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionStartDate;

    /**
     * 生产日期结束时间
     */
    @ApiModelProperty(name="endTime" ,value="生产日期结束时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionEndDate;

    /**
     * 过期日期开始时间
     */
    @ApiModelProperty(name="endTime" ,value="过期日期开始时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredStartDate;

    /**
     * 过期日期结束时间
     */
    @ApiModelProperty(name="endTime" ,value="过期日期结束时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredEndDate;


    /**
     * 入库日期开始时间
     */
    @ApiModelProperty(name="receivingStartDate" ,value="入库日期开始时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivingStartDate;

    /**
     * 入库日期结束时间
     */
    @ApiModelProperty(name="receivingEndDate" ,value="入库日期结束时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivingEndDate;

    /**
     * 出库日期开始时间
     */
    @ApiModelProperty(name="deliverStartDate" ,value="出库日期开始时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverStartDate;

    /**
     * 出库日期结束时间
     */
    @ApiModelProperty(name="deliverEndDate" ,value="出库日期结束时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliverEndDate;

    /**
     * 库存状态
     */
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    private String inventoryStatusName;

}
