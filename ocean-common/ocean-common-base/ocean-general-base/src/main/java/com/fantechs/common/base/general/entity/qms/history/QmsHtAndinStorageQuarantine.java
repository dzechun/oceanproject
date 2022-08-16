package com.fantechs.common.base.general.entity.qms.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 入库待检历史表
 * qms_ht_andin_storage_quarantine
 * @author jbb
 * @date 2021-01-09 20:25:01
 */
@Data
@Table(name = "qms_ht_andin_storage_quarantine")
public class QmsHtAndinStorageQuarantine implements Serializable {
    /**
     * 入库待检历史ID
     */
    @ApiModelProperty(name="htAndinStorageQuarantineId",value = "入库待检历史ID")
    @Excel(name = "入库待检历史ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_andin_storage_quarantine_id")
    private Long htAndinStorageQuarantineId;

    /**
     * 入库待检ID
     */
    @ApiModelProperty(name="andinStorageQuarantineId",value = "入库待检ID")
    @Excel(name = "入库待检ID", height = 20, width = 30,orderNum="")
    @Column(name = "andin_storage_quarantine_id")
    private Long andinStorageQuarantineId;

    /**
     * 栈板ID
     */
    @ApiModelProperty(name="palletId",value = "栈板ID")
    @Excel(name = "栈板ID", height = 20, width = 30,orderNum="")
    @Column(name = "pallet_id")
    private Long palletId;

    /**
     * 待检区域ID
     */
    @ApiModelProperty(name="inspectionWaitingAreaId",value = "待检区域ID")
    @Excel(name = "待检区域ID", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_waiting_area_id")
    private Long inspectionWaitingAreaId;

    /**
     * 操作
     */
    @ApiModelProperty(name="operation",value = "操作")
    @Excel(name = "操作", height = 20, width = 30,orderNum="")
    private String operation;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long organizationId;

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

    private static final long serialVersionUID = 1L;
}
