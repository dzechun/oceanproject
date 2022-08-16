package com.fantechs.common.base.general.entity.qms.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

;

/**
 * MRB评审历史
 * @date 2020-12-24 13:47:41
 */
@Data
@Table(name = "qms_ht_mrb_review")
public class QmsHtMrbReview implements Serializable {
    /**
     * MRB评审历史ID
     */
    @ApiModelProperty(name="htMrbReviewId",value = "MRB评审历史ID")
    @Excel(name = "MRB评审历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_mrb_review_id")
    private Long htMrbReviewId;

    /**
     * MRB评审ID
     */
    @ApiModelProperty(name="mrbReviewId",value = "MRB评审ID")
    @Excel(name = "MRB评审ID", height = 20, width = 30)
    @Column(name = "mrb_review_id")
    private Long mrbReviewId;

    /**
     * 评审单号
     */
    @ApiModelProperty(name="mrbReviewCode",value = "评审单号")
    @Excel(name = "评审单号", height = 20, width = 30)
    @Column(name = "mrb_review_code")
    private String mrbReviewCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 质检单ID
     */
    @ApiModelProperty(name="qualityInspectionId",value = "质检单ID")
    @Excel(name = "质检单ID", height = 20, width = 30)
    @Column(name = "quality_inspection_id")
    private Long qualityInspectionId;

    /**
     * 单据类型 0-来料检验 1-其他
     */
    @ApiModelProperty(name="receiptsType",value = "单据类型 0-来料检验 1-其他")
    @Excel(name = "单据类型 0-来料检验 1-其他", height = 20, width = 30)
    @Column(name = "receipts_type")
    private Byte receiptsType;

    /**
     * 状态（0、审核中 1、已审核 2、未审核）
     */
    @ApiModelProperty(name="receiptsStatus",value = "状态（0、审核中 1、已审核 2、未审核）")
    @Excel(name = "状态（0、审核中 1、已审核 2、未审核）", height = 20, width = 30)
    @Column(name = "receipts_status")
    private Byte receiptsStatus;

    /**
     * 评审部门
     */
    @ApiModelProperty(name="reviewDept",value = "评审部门")
    @Excel(name = "评审部门", height = 20, width = 30)
    @Column(name = "review_dept")
    private Long reviewDept;

    /**
     * MRB结果
     */
    @ApiModelProperty(name="reviewResult",value = "MRB结果")
    @Excel(name = "MRB结果", height = 20, width = 30)
    @Column(name = "review_result")
    private Long reviewResult;

    /**
     * 数量
     */
    @ApiModelProperty(name="quantity",value = "数量")
    @Excel(name = "数量", height = 20, width = 30)
    private BigDecimal quantity;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;

}
