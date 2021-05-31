package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 分拣单表
 * smt_sorting_list
 * @author 53203
 * @date 2020-12-08 17:44:15
 */
@Data
@Table(name = "ptl_sorting")
public class PtlSorting extends ValidGroup implements Serializable {
    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingId",value = "分拣单Id")
    @Excel(name = "分拣单Id", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "sorting_id")
    @NotNull(groups = update.class,message = "分拣单Id不能为空")
    private Long sortingId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingCode",value = "任务号")
    @Excel(name = "任务号", height = 20, width = 30,orderNum="")
    @Column(name = "sorting_code")
    private String sortingCode;

    @Column(name = "source_sys")
    private String sourceSys;

    @Column(name = "source_sys_id")
    private String sourceSysId;

    /**
     * 单据类型（1-拣料单 2-调拨单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-拣料单 2-调拨单）")
    @Excel(name = "单据类型（1-拣料单 2-调拨单）", height = 20, width = 30,orderNum="")
    @Column(name = "order_type")
    private Byte orderType;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="")
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="")
    @Column(name = "material_name")
    private String materialName;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_name")
    private String warehouseName;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Excel(name = "库位ID", height = 20, width = 30,orderNum="")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="")
    @Column(name = "storage_code")
    private String storageCode;

    /**
     * 分拣数量
     */
    @ApiModelProperty(name="sortingQty",value = "包装数量")
    @Excel(name = "包装数量", height = 20, width = 30,orderNum="")
    @Column(name = "sorting_qty")
    private BigDecimal sortingQty;

    /**
     * 包装单位
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位")
    @Excel(name = "包装单位", height = 20, width = 30,orderNum="")
    @Column(name = "packing_unit_name")
    private String packingUnitName;

    /**
     * 整或者零(0-零 1-整)
     */
    @ApiModelProperty(name="wholeOrScattered",value = "整或者零(0-零 1-整)")
    @Excel(name = "整或者零(0-零 1-整)", height = 20, width = 30,orderNum="")
    @Column(name = "whole_or_scattered")
    private Byte wholeOrScattered;

    /**
     * 是否已打印条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifAlreadyPrint",value = "是否已打印条码(0-否 1-是)")
    @Excel(name = "是否已打印条码(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "if_already_print")
    private Byte ifAlreadyPrint;

    /**
     * 状态(0-待激活 1-已激活 2-已完成 3-异常)
     */
    @ApiModelProperty(name="status",value = "状态(0-待激活 1-已激活 2-已完成 3-异常)")
    @Excel(name = "状态(0-待激活 1-已激活 2-已完成 3-异常)", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人Id
     */
    @ApiModelProperty(name="createUserId",value = "创建人Id")
    @Excel(name = "创建人Id", height = 20, width = 30,orderNum="")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0-删除 1-正常）
     */
    @ApiModelProperty(name="isDetele",value = "逻辑删除（0-删除 1-正常）")
    @Excel(name = "逻辑删除（0-删除 1-正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_detele")
    private Byte isDetele;

    private String remark;

    /**
     * 是否客户端传入(0-是，1-否)
     */
    @ApiModelProperty(name="status",value = "是否客户端传入(0-是，1-否)")
    @Transient
    private Byte updateStatus;

    private static final long serialVersionUID = 1L;
}