package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

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
     * 相关单号
     */
    @ApiModelProperty(name="relevanceOrderCode",value = "相关单号")
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
    private Date receivingDate;

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


    private static final long serialVersionUID = 1L;
}
