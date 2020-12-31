package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 不良品MRB评审表
 * @date 2020-12-28 15:15:07
 */
@Data
@Table(name = "qms_rejects_mrb_review")
public class QmsRejectsMrbReview extends ValidGroup implements Serializable {
    /**
     * 不良品MRB评审ID
     */
    @ApiModelProperty(name="rejectsMrbReviewId",value = "不良品MRB评审ID")
    @Id
    @Column(name = "rejects_mrb_review_id")
    private Long rejectsMrbReviewId;

    /**
     * 评审单号
     */
    @ApiModelProperty(name="mrbReviewCode",value = "评审单号")
    @Excel(name = "评审单号", height = 20, width = 30,orderNum="1")
    @Column(name = "rejects_mrb_review_code")
    private String rejectsMrbReviewCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 质检单ID
     */
    @ApiModelProperty(name="qualityInspectionId",value = "质检单ID")
    @Column(name = "quality_inspection_id")
    private Long qualityInspectionId;

    /**
     * 单据类型 0-在库检验 1-其他
     */
    @ApiModelProperty(name="receiptsType",value = "单据类型 0-在库检验 1-其他")
    @Excel(name = "单据类型 0-在库检验 1-其他", height = 20, width = 30,orderNum="5")
    @Column(name = "receipts_type")
    private Byte receiptsType;

    /**
     * 状态（0、审核中 1、已审核 2、未审核）
     */
    @ApiModelProperty(name="receiptsStatus",value = "状态（0、审核中 1、已审核 2、未审核）")
    @Excel(name = "状态（0、审核中 1、已审核 2、未审核）", height = 20, width = 30,orderNum="6")
    @Column(name = "receipts_status")
    private Byte receiptsStatus;

    /**
     * 评审部门
     */
    @ApiModelProperty(name="reviewDept",value = "评审部门")
    @Column(name = "review_dept")
    private Long reviewDept;

    /**
     * MRB结果 0-挑选 1-其他
     */
    @ApiModelProperty(name="reviewResult",value = "MRB结果 0-挑选 1-其他")
    @Excel(name = "MRB结果 0-挑选 1-其他", height = 20, width = 30,orderNum="8")
    @Column(name = "review_result")
    private Byte reviewResult;

    /**
     * 挑选数量
     */
    @ApiModelProperty(name="selectedNumber",value = "挑选数量")
    @Excel(name = "挑选数量", height = 20, width = 30,orderNum="9")
    @Column(name = "selected_number")
    private BigDecimal selectedNumber;

    /**
     * 挑选人
     */
    @ApiModelProperty(name="selectedPerson",value = "挑选人")
    @Column(name = "selected_person")
    private Long selectedPerson;

    /**
     * 挑选时间
     */
    @ApiModelProperty(name="selectedTime",value = "挑选时间")
    @Excel(name = "挑选时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "selected_time")
    private Date selectedTime;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="11")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
