package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 上料单明细表
 * smt_loading_det
 * @author 86178
 * @date 2021-01-09 16:29:47
 */
@Data
@Table(name = "ptl_loading_det")
public class PtlLoadingDet extends ValidGroup implements Serializable {
    /**
     * 上料单明细Id
     */
    @ApiModelProperty(name="loadingDetId",value = "上料单明细Id")
    @Id
    @Column(name = "loading_det_id")
    @NotNull(groups = ValidGroup.update.class, message = "上料单明细Id不能为空")
    @NotNull(groups = ValidGroup.submit.class, message = "物料Id不能为空")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Long loadingDetId;

    /**
     * 上料单Id
     */
    @ApiModelProperty(name="loadingId",value = "上料单Id")
    @Column(name = "loading_id")
    private Long loadingId;

    /**
     * 物料Id
     */
    @ApiModelProperty(name="materialId",value = "物料Id")
    @Column(name = "material_id")
    @NotNull(groups = ValidGroup.submit.class, message = "物料Id不能为空")
    private Long materialId;

    /**
     * 计划上料数量
     */
    @ApiModelProperty(name="planQty",value = "计划上料数量")
    @Excel(name = "计划上料数量", height = 20, width = 30,orderNum="5")
    @Column(name = "plan_qty")
    @NotNull(groups = ValidGroup.submit.class, message = "计划上料数量不能为空")
    @NotNull(message = "计划上料数量不能为空")
    private BigDecimal planQty;

    /**
     * 实际上料数量
     */
    @ApiModelProperty(name="actualQty",value = "实际上料数量")
    @Excel(name = "实际上料数量", height = 20, width = 30,orderNum="6")
    @Column(name = "actual_qty")
    @NotNull(groups = ValidGroup.submit.class, message = "实际上料数量不能为空")
    private BigDecimal actualQty;

    /**
     * 状态（0-未开始 1-上料中 2-部分完成 3-已完成）
     */
    @ApiModelProperty(name="status",value = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）")
    @Excel(name = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）", height = 20, width = 30,orderNum="7")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "备注", height = 20, width = 30,orderNum="16")
    private String remark;

    /**
     * 组织Id
     */
    @ApiModelProperty(name="orgId",value = "组织Id")
    @Column(name = "org_id")
    private Long orgId;

    private static final long serialVersionUID = 1L;
}