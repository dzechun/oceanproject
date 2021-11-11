package com.fantechs.common.base.general.entity.callagv;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 任务条码表
 * call_agv_agv_task_barcode
 * @author admin
 * @date 2021-11-11 14:13:16
 */
@Data
@Table(name = "call_agv_agv_task_barcode")
public class CallAgvAgvTaskBarcode extends ValidGroup implements Serializable {
    /**
     * 任务条码表ID
     */
    @ApiModelProperty(name="agvTaskBarcodeId",value = "任务条码表ID")
    @Excel(name = "任务条码表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "agv_task_barcode_id")
    private Long agvTaskBarcodeId;

    /**
     * AGV任务ID
     */
    @ApiModelProperty(name="agvTaskId",value = "AGV任务ID")
    @Excel(name = "AGV任务ID", height = 20, width = 30,orderNum="") 
    @Column(name = "agv_task_id")
    private Long agvTaskId;

    /**
     * 条码ID
     */
    @ApiModelProperty(name="barcodeId",value = "条码ID")
    @Excel(name = "条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_id")
    private Long barcodeId;

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

    /**
     * 物料型号
     */
    @ApiModelProperty(name="productModel",value = "物料型号")
    @Transient
    private String productModel;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Transient
    private BigDecimal qty;

    private static final long serialVersionUID = 1L;
}