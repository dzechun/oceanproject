package com.fantechs.common.base.general.entity.om;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

@Table(name = "mes_schedule_detail")
@Data
public class MesScheduleDetail implements Serializable {
    /**
    * 排产详情ID
    */
    @ApiModelProperty(value = "排产详情ID",example = "排产详情ID")
    @Column(name = "schedule_detail_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Excel(name = "排产详情ID")
    private Long scheduleDetailId;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "org_id")
    @Excel(name = "组织代码id")
    private Long organizationId;

    /**
    * 排产ID
    */
    @ApiModelProperty(value = "排产ID",example = "排产ID")
    @Column(name = "schedule_id")
    @Excel(name = "排产ID")
    private Long scheduleId;

    /**
    * 工单ID
    */
    @ApiModelProperty(value = "工单ID",example = "工单ID")
    @Column(name = "work_order_id")
    @Excel(name = "工单ID")
    private Long workOrderId;

    /**
    * 销售订单ID
    */
    @ApiModelProperty(value = "销售订单ID",example = "销售订单ID")
    @Column(name = "order_id")
    @Excel(name = "销售订单ID")
    private Long orderId;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",example = "备注")
    @Excel(name = "备注")
    private String remark;

    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID",example = "创建人ID")
    @Column(name = "create_user_id")
    @Excel(name = "创建人ID")
    private Long createUserId;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",example = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private java.util.Date createTime;

    /**
    * 修改人ID
    */
    @ApiModelProperty(value = "修改人ID",example = "修改人ID")
    @Column(name = "modified_user_id")
    @Excel(name = "修改人ID")
    private Long modifiedUserId;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",example = "修改时间")
    @Column(name = "modified_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

}