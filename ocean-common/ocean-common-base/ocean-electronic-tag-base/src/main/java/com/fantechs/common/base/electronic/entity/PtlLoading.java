package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;

/**
 * 上料单
 * smt_loading
 * @author 86178
 * @date 2021-01-09 16:29:46
 */
@Data
@Table(name = "ptl_loading")
public class PtlLoading extends ValidGroup implements Serializable {
    /**
     * 上料单Id
     */
    @ApiModelProperty(name="loadingId",value = "上料单Id")
    @Id
    @Column(name = "loading_id")
    @NotNull(groups = ValidGroup.update.class, message = "上料单Id不能为空")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Long loadingId;

    /**
     * 上料单号
     */
    @ApiModelProperty(name="loadingCode",value = "上料单号")
    @Excel(name = "上料单号", height = 20, width = 30,orderNum="1")
    @Column(name = "loading_code")
    @NotBlank(message = "上料单号不能为空")
    private String loadingCode;

    /**
     * 来源系统名称
     */
    @ApiModelProperty(name="sourceSys",value = "来源系统名称")
    @Excel(name = "来源系统名称", height = 20, width = 30,orderNum="2")
    @Column(name = "source_sys")
    @NotBlank(message = "来源系统名称不能为空")
    private String sourceSys;

    /**
     * 来源系统行Id
     */
    @ApiModelProperty(name="sourceSysId",value = "来源系统行Id")
    @Column(name = "source_sys_id")
    private String sourceSysId;

    /**
     * 单据类型（1-采购单 2-入库单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-采购单 2-入库单）")
    @Excel(name = "单据类型（1-采购单 2-入库单）", height = 20, width = 30,orderNum="3")
    @Column(name = "order_type")
    @NotNull(message = "单据类型（1-采购单 2-入库单）不能为空")
    private Byte orderType;

    /**
     * 状态（0-未开始 1-上料中 2-部分完成 3-已完成）
     */
    @ApiModelProperty(name="status",value = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）")
    @Excel(name = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）", height = 20, width = 30,orderNum="4")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
    private String remark;

    /**
     * 组织Id
     */
    @ApiModelProperty(name="orgId",value = "组织Id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 上料单明细
     */
    @ApiModelProperty(name="smtLoadingDetDtoList",value = "上料单明细")
    @Transient
    private List<PtlLoadingDetDto> ptlLoadingDetDtoList;

    private static final long serialVersionUID = 1L;
}