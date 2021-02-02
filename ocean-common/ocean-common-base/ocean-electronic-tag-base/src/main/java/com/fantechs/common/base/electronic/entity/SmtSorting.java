package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 分拣单表
 * smt_sorting_list
 * @author 53203
 * @date 2020-12-08 17:44:15
 */
@Data
@Table(name = "smt_sorting")
public class SmtSorting extends ValidGroup implements Serializable {
    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingId",value = "分拣单Id")
    @Id
    @Column(name = "sorting_id")
    @NotNull(groups = update.class,message = "分拣单Id不能为空")
    private Long sortingId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingCode",value = "分拣单号")
    @Excel(name = "分拣单号", height = 20, width = 30,orderNum="1")
    @Column(name = "sorting_code")
    @NotBlank(message = "分拣单号不能为空")
    private String sortingCode;

    /**
     * 来源系统名称
     */
    @ApiModelProperty(name="sourceSysId",value = "来源系统名称")
    @Excel(name = "来源系统名称", height = 20, width = 30,orderNum="2")
    @Column(name = "source_sys")
    @NotBlank(message = "来源系统名称不能为空")
    private String sourceSys;

    /**
     * 来源系统行ID
     */
    @ApiModelProperty(name="sourceSysId",value = "来源系统行ID")
    @Column(name = "source_sys_id")
    private String sourceSysId;

    /**
     * 单据类型（1-调拨单 2领料单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-调拨单 2领料单）")
    @Excel(name = "单据类型（1-调拨单 2领料单）", height = 20, width = 30,orderNum="3")
    @Column(name = "order_type")
    @NotNull(message = "单据类型不能为空")
    private Byte orderType;

    /**
     * 工单号(预留)
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号(预留)")
    @Excel(name = "工单号(预留)", height = 20, width = 30,orderNum="4")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="5")
    @Column(name = "material_code")
    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    /**
     * 分拣数量
     */
    @ApiModelProperty(name="quantity",value = "分拣数量")
    @Excel(name = "分拣数量", height = 20, width = 30,orderNum="8")
    @NotNull(message = "分拣数量不能为空")
    private BigDecimal quantity;

    /**
     * 状态(0-未开始，1-分拣中 2-已完成)
     */
    @ApiModelProperty(name="status",value = "状态(0-未开始，1-分拣中 2-已完成)")
    @Excel(name = "状态(0-未开始，1-分拣中 2-已完成)", height = 20, width = 30,orderNum="9")
    private Byte status;

    /**
     * 创建人Id
     */
    @ApiModelProperty(name="createUserId",value = "创建人Id")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="17",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0-删除 1-正常）
     */
    @ApiModelProperty(name="isDetele",value = "逻辑删除（0-删除 1-正常）")
    @Column(name = "is_detele")
    private Byte isDetele;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 是否客户端传入(0-是，1-否)
     */
    @ApiModelProperty(name="status",value = "是否客户端传入(0-是，1-否)")
    @Transient
    private Byte updateStatus;

    private static final long serialVersionUID = 1L;
}