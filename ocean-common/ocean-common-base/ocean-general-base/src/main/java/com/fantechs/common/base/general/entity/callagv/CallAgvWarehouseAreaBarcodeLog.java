package com.fantechs.common.base.general.entity.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * 库区条码出入库日志表
 * call_agv_warehouse_area_barcode_log
 * @author 86178
 * @date 2021-12-23 10:23:00
 */
@Data
@Table(name = "call_agv_warehouse_area_barcode_log")
public class CallAgvWarehouseAreaBarcodeLog extends ValidGroup implements Serializable {
    /**
     * 库区条码日志表ID
     */
    @ApiModelProperty(name="warehouseAreaBarcodeLogId",value = "库区条码日志表ID")
    @Excel(name = "库区条码日志表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "warehouse_area_barcode_log_id")
    private Long warehouseAreaBarcodeLogId;

    /**
     * 条码ID
     */
    @ApiModelProperty(name="barcodeId",value = "条码ID")
    @Excel(name = "条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_id")
    private Long barcodeId;

    /**
     * 库区ID
     */
    @ApiModelProperty(name="warehouseAreaId",value = "库区ID")
    @Excel(name = "库区ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_area_id")
    private Long warehouseAreaId;

    /**
     * 操作类型(1-入库 2-出库)
     */
    @ApiModelProperty(name="operatorType",value = "操作类型(1-入库 2-出库)")
    @Excel(name = "操作类型(1-入库 2-出库)", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_type")
    private Byte operatorType;

    /**
     * 操作时间
     */
    @ApiModelProperty(name="operatorTime",value = "操作时间")
    @Excel(name = "操作时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "operator_time")
    private Date operatorTime;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作用户ID")
    @Excel(name = "操作用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_user_id")
    private Long operatorUserId;

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