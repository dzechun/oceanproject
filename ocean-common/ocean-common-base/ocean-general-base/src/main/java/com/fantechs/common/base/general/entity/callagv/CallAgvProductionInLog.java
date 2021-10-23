package com.fantechs.common.base.general.entity.callagv;

import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 生产入库明细报表
 * call_agv_production_in_log
 * @author 86178
 * @date 2021-10-22 10:08:18
 */
@Data
@Table(name = "call_agv_production_in_log")
public class CallAgvProductionInLog extends ValidGroup implements Serializable {
    /**
     * 生产入库记录ID
     */
    @ApiModelProperty(name="productionInLogId",value = "生产入库记录ID")
    @Id
    @Column(name = "production_in_log_id")
    private Long productionInLogId;

    /**
     * 入库仓库ID
     */
    @ApiModelProperty(name="inWarehouseId",value = "入库仓库ID")
    @Column(name = "in_warehouse_id")
    private Long inWarehouseId;

    /**
     * 出库仓库ID
     */
    @ApiModelProperty(name="outWarehouseId",value = "出库仓库ID")
    @Column(name = "out_warehouse_id")
    private Long outWarehouseId;

    /**
     * 入库仓库区域ID
     */
    @ApiModelProperty(name="inWarehouseAreaId",value = "入库仓库区域ID")
    @Column(name = "in_warehouse_area_id")
    private Long inWarehouseAreaId;

    /**
     * 出库仓库区域ID
     */
    @ApiModelProperty(name="outWarehouseAreaId",value = "出库仓库区域ID")
    @Column(name = "out_warehouse_area_id")
    private Long outWarehouseAreaId;

    /**
     * 入库库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "入库库位ID")
    @Column(name = "in_storage_id")
    private Long inStorageId;

    /**
     * 出库库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "出库库位ID")
    @Column(name = "out_storage_id")
    private Long outStorageId;

    /**
     * 条码表ID
     */
    @ApiModelProperty(name="barcodeId",value = "条码表ID")
    @Column(name = "barcode_id")
    private Long barcodeId;

    /**
     * 起始配送点ID
     */
    @ApiModelProperty(name="startStorageTaskPointId",value = "起始配送点ID")
    @Column(name = "start_storage_task_point_id")
    private Long startStorageTaskPointId;

    /**
     * 目标配送点ID
     */
    @ApiModelProperty(name="endStorageTaskPointId",value = "目标配送点ID")
    @Column(name = "end_storage_task_point_id")
    private Long endStorageTaskPointId;

    /**
     * 操作类型(1-入库 2-出库)
     */
    @ApiModelProperty(name="operateType",value = "操作类型(1-入库 2-出库)")
    @Column(name = "operate_type")
    private Byte operateType;

    /**
     * 操作时间
     */
    @ApiModelProperty(name="operateTime",value = "操作时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "operate_time")
    private Date operateTime;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}