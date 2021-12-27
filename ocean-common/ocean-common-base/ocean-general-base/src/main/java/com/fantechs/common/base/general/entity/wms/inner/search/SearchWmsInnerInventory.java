package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchWmsInnerInventory extends BaseQuery implements Serializable {

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    /**
     * 制造编码
     */
    @ApiModelProperty(name="makeCode",value = "制造编码")
    private String makeCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 库位名称
     */
    @ApiModelProperty(name="storageName",value = "库位名称")
    private String storageName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 单据号
     */
    @ApiModelProperty(name="relevanceOrderCode",value = "单据号")
    private String relevanceOrderCode;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    private String palletCode;

    /**
     * 收货日期
     */
    @ApiModelProperty(name="receivingDate",value = "收货日期")
    private String receivingDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    private String batchCode;

    /**
     * 采购单号
     */
    @ApiModelProperty(name="poCode",value = "采购单号")
    private String poCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    /**
     * 销售单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售单号")
    private String salesOrderCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 仓库id
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name = "inventoryStatusId",value = "库存状态ID")
    private Long inventoryStatusId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name = "storageId",value = "库位ID")
    private Long storageId;

    /**
     * 库存ID
     */
    @ApiModelProperty(name="inventoryId",value = "库存ID")
    private Long inventoryId;

    /**
     * 检验单号
     */
    @ApiModelProperty(name = "inspectionOrderCode",value = "检验单号")
    private String inspectionOrderCode;

    /**
     * 库位编码
     */
    @ApiModelProperty(name = "storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 锁定状态(0-否 1-是)
     */
    @ApiModelProperty(name="lockStatus",value = "锁定状态(0-否 1-是)")
    private Byte lockStatus;

    /**
     * 作业状态(1正常 2待出)
     */
    @ApiModelProperty(name="jobStatus",value = "作业状态(1正常 2待出)")
    private Byte jobStatus;

    /**
     * 盘点锁(0-否 1-是)
     */
    @ApiModelProperty(name="stockLock",value = "盘点锁(0-否 1-是)")
    private Byte stockLock;

    /**
     * 库位类型（1-存货 2-收货 3-发货）
     */
    @ApiModelProperty(name="storageType",value = "库位类型（1-存货 2-收货 3-发货）")
    private Byte storageType;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode",value = "合同号")
    private String contractCode;

    /**
     * 规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    private String spec;

    /**
     * 请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;

    /**
     * 库存状态
     */
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    private String inventoryStatusName;

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

    private static final long serialVersionUID = 1L;
}
