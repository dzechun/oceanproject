package com.fantechs.common.base.general.dto.wms.inner.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author
 * @Date 2021/12/16
 */
@Data
public class WmsInnerJobOrderImport implements Serializable {

    /**
     * 仓库(必填)
     */
    @Excel(name = "仓库(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="warehouseName" ,value="仓库(必填)")
    private String warehouseName;

    /**
     * 仓库ID
     */
    @Excel(name = "仓库ID",  height = 20, width = 30)
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    private Long warehouseId;

    /**
     * 作业单号(必填)
     */
    @Excel(name = "作业单号(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="jobOrderCode" ,value="作业单号(必填)")
    private String jobOrderCode;

    /**
     * 工作人员
     */
    @Excel(name = "工作人员",  height = 20, width = 30)
    @ApiModelProperty(name="workName" ,value="工作人员")
    private String workName;

    /**
     * 来源单据类型
     */
    @Excel(name = "来源单据类型",  height = 20, width = 30)
    @ApiModelProperty(name="sourceSysOrderTypeCode" ,value="来源单据类型")
    private String sourceSysOrderTypeCode;

    /**
     * 移出库位(必填)
     */
    @Excel(name = "移出库位(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="outStorageName" ,value="移出库位(必填)")
    private String outStorageName;

    /**
     * 移出库位ID
     */
    @Excel(name = "移出库位ID",  height = 20, width = 30)
    @ApiModelProperty(name="outStorageId" ,value="移出库位ID")
    private Long outStorageId;

    /**
     * 物料编码(必填)
     */
    @Excel(name = "物料编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="物料编码(必填)")
    private String materialCode;

    /**
     * 物料ID
     */
    @Excel(name = "物料ID",  height = 20, width = 30)
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",  height = 20, width = 30)
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料规格
     */
    @Excel(name = "物料规格",  height = 20, width = 30)
    @ApiModelProperty(name="materialSpec" ,value="物料规格")
    private String materialSpec;

    /**
     * 包装单位
     */
    @Excel(name = "包装单位",  height = 20, width = 30)
    @ApiModelProperty(name="packingUnitName" ,value="包装单位")
    private String packingUnitName;

    /**
     * 计划数量(必填)
     */
    @Excel(name = "计划数量(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="planQty" ,value="计划数量(必填)")
    private String planQty;
}
