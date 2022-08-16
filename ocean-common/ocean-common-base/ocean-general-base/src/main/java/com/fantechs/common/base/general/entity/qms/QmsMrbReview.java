package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * MRB评审表
 * @date 2020-12-24 11:38:53
 */
@Data
@Table(name = "qms_mrb_review")
public class QmsMrbReview extends ValidGroup implements Serializable {
    /**
     * MRB评审ID
     */
    @ApiModelProperty(name="mrbReviewId",value = "MRB评审ID")
    @Id
    @Column(name = "mrb_review_id")
    private Long mrbReviewId;

    /**
     * 评审单号
     */
    @ApiModelProperty(name="mrbReviewCode",value = "评审单号")
    @Excel(name = "评审单号", height = 20, width = 30,orderNum="1")
    @Column(name = "mrb_review_code")
    private String mrbReviewCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    /**
     * 质检单ID
     */
    @ApiModelProperty(name="qualityInspectionId",value = "质检单ID")
    @Column(name = "quality_inspection_id")
    @NotNull(message = "质检单ID不能为空")
    private Long qualityInspectionId;

    /**
     * 单据类型 0-来料检验 1-其他
     */
    @ApiModelProperty(name="receiptsType",value = "单据类型 0-来料检验 1-其他")
    @Excel(name = "单据类型 0-来料检验 1-其他", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "评审部门", height = 20, width = 30,orderNum="7")
    @Column(name = "review_dept")
    private Long reviewDept;

    /**
     * MRB结果
     */
    @ApiModelProperty(name="reviewResult",value = "MRB结果")
    @Excel(name = "MRB结果", height = 20, width = 30,orderNum="8")
    @Column(name = "review_result")
    private Long reviewResult;

    /**
     * 数量
     */
    @ApiModelProperty(name="quantity",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="9")
    private BigDecimal quantity;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="11")
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
