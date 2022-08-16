package com.fantechs.common.base.general.entity.om;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;

@Table(name = "mes_schedule")
@Data
public class MesSchedule implements Serializable {
    /**
    * 排产单id
    */
    @ApiModelProperty(value = "排产单id",example = "排产单id")
    @Column(name = "schedule_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long scheduleId;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "org_id")
    @Excel(name = "组织代码id")
    private Long organizationId;

    /**
    * 排产单编号
    */
    @ApiModelProperty(value = "排产单编号",example = "排产单编号")
    @Column(name = "schedule_code")
    @Excel(name = "排产单编号")
    private String scheduleCode;

    /**
     * 产线id
     */
    @ApiModelProperty(value = "产线id",example = "产线id")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
    * 排产总数
    */
    @ApiModelProperty(value = "排产总数",example = "排产总数")
    @Excel(name = "排产总数")
    private java.math.BigDecimal total;

    /**
    * 完成数
    */
    @ApiModelProperty(value = "完成数",example = "完成数")
    @Excel(name = "完成数")
    private java.math.BigDecimal finished;

    /**
    * 工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
    */
    @ApiModelProperty(value = "工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)",example = "工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)")
    @Excel(name = "工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)")
    private Byte status;

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