package com.fantechs.common.base.general.entity.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.qms.QmsPoorQualityDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 品质确认表
 * @date 2021-01-19 09:52:21
 */
@Data
@Table(name = "qms_quality_confirmation")
public class QmsQualityConfirmation extends ValidGroup implements Serializable {
    /**
     * 品质确认ID
     */
    @ApiModelProperty(name="qualityConfirmationId",value = "品质确认ID")
    @Id
    @Column(name = "quality_confirmation_id")
    private Long qualityConfirmationId;

    /**
     * 品质确认单号
     */
    @ApiModelProperty(name="qualityConfirmationCode",value = "品质确认单号")
    @Excel(name = "品质确认单号", height = 20, width = 30,orderNum="3")
    @Column(name = "quality_confirmation_code")
    private String qualityConfirmationCode;

    /**
     * 流程卡ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "流程卡ID")
    @Column(name = "work_order_card_pool_id")
    private Long workOrderCardPoolId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 品质类型（0、品质确认 1、品质抽检 ）
     */
    @ApiModelProperty(name="qualityType",value = "品质类型（0、品质确认 1、品质抽检 ）")
    @Column(name = "quality_type")
    private Byte qualityType;

    /**
     * 总品质确认数量
     */
    @ApiModelProperty(name="total",value = "总品质确认数量")
    @Column(name = "total")
    private BigDecimal total;

    /**
     * 合格总数量
     */
    @ApiModelProperty(name="totalQualified",value = "合格总数量")
    @Column(name = "total_qualified")
    private BigDecimal totalQualified;

    /**
     * 抽检数量
     */
    @ApiModelProperty(name="quantityDeclared",value = "抽检数量")
    @Column(name = "quantity_declared")
    private BigDecimal quantityDeclared;

    /**
     * 合格数量
     */
    @ApiModelProperty(name="qualifiedQuantity",value = "合格数量")
    @Excel(name = "合格数量", height = 20, width = 30,orderNum="12")
    @Column(name = "qualified_quantity")
    private BigDecimal qualifiedQuantity;

    /**
     * 不合格数量
     */
    @ApiModelProperty(name="unqualifiedQuantity",value = "不合格数量")
    @Excel(name = "合格数量", height = 20, width = 30,orderNum="13")
    @Column(name = "unqualified_quantity")
    private BigDecimal unqualifiedQuantity;

    /**
     * 品质确认状态（0、待确认 1、确认中 2、已确认）
     */
    @ApiModelProperty(name="affirmStatus",value = "品质确认状态（0、待确认 1、确认中 2、已确认）")
    @Excel(name = "品质确认状态",orderNum = "14",height = 20, width = 30,replace = {"待确认_0","确认中_1","已确认_2"})
    @Column(name = "affirm_status")
    private Byte affirmStatus;

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
    @Excel(name = "备注", height = 20, width = 30,orderNum="15")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态", height = 20, width = 30,orderNum="16",replace = {"无效_0","有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="18",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="20",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 不良现象集合
     */
    @ApiModelProperty(name="list",value = "不良现象集合")
    private List<QmsPoorQualityDto> seledBadItemList;

    private static final long serialVersionUID = 1L;
}
