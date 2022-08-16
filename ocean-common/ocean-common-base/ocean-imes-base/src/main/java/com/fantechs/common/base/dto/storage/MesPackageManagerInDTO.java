package com.fantechs.common.base.dto.storage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/9 20:22
 * @Description: 供成品入库使用
 * @Version: 1.0
 */
@Data
public class MesPackageManagerInDTO {
    /**
     * 包装管理id
     */
    @ApiModelProperty(value = "包装管理id",example = "包装管理id")
    private Long packageManagerId;
    /**
     * 包装管理号
     */
    @ApiModelProperty(value = "包装管理号",example = "包装管理号")
    private String packageManagerCode;
    /**
     * 包装类型（1、包箱 2、栈板）
     */
    @ApiModelProperty(value = "包装类型（1、包箱 2、栈板）",example = "包装类型（1、包箱 2、栈板）")
    private Byte type;
    /**
     * 条码
     */
    @ApiModelProperty(value = "条码",example = "条码")
    private String barCode;
    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id",example = "工单id")
    private Long workOrderId;
    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号",example = "工单号")
    private String workOrderCode;
    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码",example = "物料编码")
    private String materialCode;
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id",example = "物料id")
    private Long materialId;
    /**
     * 物料描述
     */
    @ApiModelProperty(value = "物料描述",example = "物料描述")
    private String materialDesc;
    /**
     * 储位ID
     */
    @ApiModelProperty(value = "储位ID",example = "储位ID")
    private Long storageId;
    /**
     * 储位名称
     */
    @ApiModelProperty(value = "储位名称",example = "储位名称")
    private String storageName;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID",example = "仓库ID")
    private Long warehouseId;
    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称",example = "仓库名称")
    private String warehouseName;
    /**
     * 仓库区域ID
     */
    @ApiModelProperty(value = "仓库区域ID",example = "仓库区域ID")
    private Long warehouseAreaId;
    /**
     * 仓库区域名称
     */
    @ApiModelProperty(value = "仓库区域名称",example = "仓库区域名称")
    private String warehouseAreaName;
    /**
     * 箱数
     */
    @ApiModelProperty(value = "箱数",example = "箱数")
    private java.math.BigDecimal boxCount;
    /**
     * 入库总数
     */
    @ApiModelProperty(value = "入库总数",example = "入库总数")
    private java.math.BigDecimal total;
    /**
     * 包装单位id
     */
    @ApiModelProperty(value = "包装单位id",example = "包装单位id")
    private Long packingUnitId;

    /**
     * 合同号
     */
    @ApiModelProperty(value = "合同号",example = "合同号")
    private String contractCode;
}
