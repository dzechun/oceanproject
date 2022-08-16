package com.fantechs.common.base.general.entity.qms;

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
import java.util.List;

/**
 * PDA首检表
 * @date 2021-01-06 19:06:02
 */
@Data
@Table(name = "qms_first_inspection")
public class QmsFirstInspection  extends ValidGroup implements Serializable {
    /**
     * PDA首检ID
     */
    @ApiModelProperty(name="firstInspectionId",value = "PDA首检ID")
    @Id
    @Column(name = "first_inspection_id")
    private Long firstInspectionId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "`work_order_id`")
    private Long workOrderId;

    /**
     * 首检单号
     */
    @ApiModelProperty(name="firstInspectionCode",value = "首检单号")
    @Excel(name = "首检单号", height = 20, width = 30,orderNum="1")
    @Column(name = "`first_inspection_code`")
    private String firstInspectionCode;

    /**
     * 挑选总数量
     */
    @ApiModelProperty(name = "selectedTotalQuantity",value = "挑选总数量")
    @Excel(name = "挑选总数量", height = 20, width = 30,orderNum="8")
    @Column(name = "selected_total_quantity")
    private BigDecimal selectedTotalQuantity;

    /**
     * 检验结果（0、未知 1、合格 2、不合格）
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果（0、未知 1、合格 2、不合格）")
    @Excel(name = "检验结果", height = 20, width = 30,orderNum="9",replace ={"未知_0","合格_1","不合格_2"})
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 处理人ID
     */
    @ApiModelProperty(name="handler",value = "处理人ID")
    private Long handler;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="documentsTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "documents_time")
    private Date documentsTime;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="receiptsType",value = "单据类型")
    @Column(name = "receipts_type")
    private Byte receiptsType;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 不合格项集合
     */
    private List<QmsDisqualification> list;

    private static final long serialVersionUID = 1L;
}
