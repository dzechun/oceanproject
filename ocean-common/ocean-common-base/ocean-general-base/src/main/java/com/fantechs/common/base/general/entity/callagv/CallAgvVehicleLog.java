package com.fantechs.common.base.general.entity.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 货架日志表
 * call_agv_vehicle_log
 * @author 86178
 * @date 2021-09-13 21:39:50
 */
@Data
@Table(name = "call_agv_vehicle_log")
public class CallAgvVehicleLog extends ValidGroup implements Serializable {
    /**
     * 货架日志表ID
     */
    @ApiModelProperty(name="vehicleLogId",value = "货架日志表ID")
    @Excel(name = "货架日志表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "vehicle_log_id")
    private Long vehicleLogId;

    /**
     * 条码ID
     */
    @ApiModelProperty(name="barcodeId",value = "条码ID")
    @Excel(name = "条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_id")
    private Long barcodeId;

    /**
     * 周转工具ID
     */
    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    @Excel(name = "周转工具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "vehicle_id")
    private Long vehicleId;

    /**
     * 配送点起点ID
     */
    @ApiModelProperty(name="startStorageTaskPointId",value = "配送点起点ID")
    @Excel(name = "配送点起点ID", height = 20, width = 30,orderNum="") 
    @Column(name = "start_storage_task_point_id")
    private Long startStorageTaskPointId;

    /**
     * 配送点目标ID
     */
    @ApiModelProperty(name="endStorageTaskPointId",value = "配送点目标ID")
    @Excel(name = "配送点目标ID", height = 20, width = 30,orderNum="") 
    @Column(name = "end_storage_task_point_id")
    private Long endStorageTaskPointId;

    /**
     * 操作类型(1-备料 2-叫料 3-解绑 4-空货架移动)
     */
    @ApiModelProperty(name="operatorType",value = "操作类型(1-备料 2-叫料 3-解绑 4-空货架移动)")
    @Excel(name = "操作类型(1-备料 2-叫料 3-解绑 4-空货架移动)", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_type")
    private Byte operatorType;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}