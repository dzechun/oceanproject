package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * PDA质检
 * qms_pda_inspection
 * @author jbb
 * @date 2021-01-07 18:50:51
 */
@Data
@Table(name = "qms_pda_inspection")
public class QmsPdaInspection extends ValidGroup implements Serializable {
    /**
     * PDA质检ID
     */
    @ApiModelProperty(name="pdaInspectionId",value = "PDA质检ID")
    @Id
    @Column(name = "pda_inspection_id")
    private Long pdaInspectionId;

    /**
     * PDA质检单号
     */
    @ApiModelProperty(name="pdaInspectionCode",value = "PDA质检单号")
    @Excel(name = "PDA质检单号", height = 20, width = 30,orderNum="1")
    @Column(name = "pda_inspection_code")
    private String pdaInspectionCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "`work_order_id`")
    private Long workOrderId;

    /**
     * 栈板ID
     */
    @ApiModelProperty(name="packageManagerId",value = "栈板ID")
    @Column(name = "package_manager_id")
    private Long packageManagerId;

    /**
     * 检验人ID
     */
    @ApiModelProperty(name="surveyorId",value = "检验人ID")
    @Column(name = "surveyor_id")
    private Long surveyorId;

    /**
     * 单据时间
     */
    @ApiModelProperty(name="documentsTime",value = "单据时间")
    @Excel(name = "单据时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "documents_time")
    private Date documentsTime;

    /**
     * 反馈人ID
     */
    @ApiModelProperty(name="feedbackUserId",value = "反馈人ID")
    @Excel(name = "反馈人ID", height = 20, width = 30)
    @Column(name = "feedback_user_id")
    private Long feedbackUserId;

    /**
     * 反馈时间
     */
    @ApiModelProperty(name="feedbackTime",value = "反馈时间")
    @Excel(name = "反馈时间", height = 20, width = 30)
    @Column(name = "feedback_time")
    private Date feedbackTime;

    /**
     * 反馈事项
     */
    @ApiModelProperty(name="feedbackMatters",value = "反馈事项")
    @Excel(name = "反馈事项", height = 20, width = 30,orderNum="8")
    @Column(name = "feedback_matters")
    private String feedbackMatters;

    /**
     * 单据类型（0、首检单 1、成品检验单）
     */
    @ApiModelProperty(name="documentsType",value = "单据类型（0、首检单 1、成品检验单）")
    @Column(name = "documents_type")
    @Excel(name = "单据类型", height = 20, width = 30,orderNum="5",replace = {"首检单_0","成品检验单_1"})
    private Byte documentsType;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * PDA质检明细对象
     */
    @ApiModelProperty(name="qmsPdaInspectionDet",value = "PDA质检明细对象")
    private QmsPdaInspectionDetDto qmsPdaInspectionDet;

    /**
     * PDA质检明细对象集合
     */
    @ApiModelProperty(name="list",value = "PDA质检明细对象集合")
    private List<QmsPdaInspectionDetDto> list;

    private static final long serialVersionUID = 1L;
}
