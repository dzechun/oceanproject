package com.fantechs.common.base.general.entity.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

@Table(name = "mes_pm_process_list_process_re")
@Data
public class MesPmProcessListProcessRe implements Serializable {
    /**
    * 流程单工序退回ID
    */
    @ApiModelProperty(value = "流程单工序退回ID",example = "流程单工序退回ID")
    @Column(name = "process_list_process_re_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long processListProcessReId;

    /**
    * 流程单工序退回编码
    */
    @ApiModelProperty(value = "流程单工序退回编码",example = "流程单工序退回编码")
    @Column(name = "process_list_process_re_code")
    @Excel(name = "过站退回编码", height = 20, width = 30,orderNum = "2")
    private String processListProcessReCode;

    /**
    * 工单流程卡任务池ID
    */
    @ApiModelProperty(value = "工单流程卡任务池ID",example = "工单流程卡任务池ID")
    @Column(name = "work_order_card_pool_id")
    private Long workOrderCardPoolId;

    /**
    * 工单条码任务池ID
    */
    @ApiModelProperty(value = "工单条码任务池ID",example = "工单条码任务池ID")
    @Column(name = "work_order_barcode_pool_id")
    private Long workOrderBarcodePoolId;

    /**
    * 操作退回工序ID
    */
    @ApiModelProperty(value = "操作退回工序ID",example = "操作退回工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
    * 指定退回工序ID
    */
    @ApiModelProperty(value = "指定退回工序ID",example = "指定退回工序ID")
    @Column(name = "re_process_id")
    private Long reProcessId;

    /**
     * 员工ID
     */
    @ApiModelProperty(name="staffId",value = "员工ID")
    private Long staffId;

    /**
    * 退回数量
    */
    @ApiModelProperty(value = "退回数量",example = "退回数量")
    @Column(name = "re_qty")
    @Excel(name = "退回数量", height = 20, width = 30,orderNum = "12")
    private java.math.BigDecimal reQty;

    /**
    * 上次报工数量
    */
    @ApiModelProperty(value = "上次报工数量",example = "上次报工数量")
    @Column(name = "pre_qty")
    @Excel(name = "上工序报工数量", height = 20, width = 30,orderNum = "13")
    private java.math.BigDecimal preQty;

    /**
    * 退回状态(0-待退回 1-退回中 2-OK)
    */
    @ApiModelProperty(value = "退回状态(0-待退回 1-退回中 2-OK)",example = "退回状态(0-待退回 1-退回中 2-OK)")
    private Byte status;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",example = "备注")
    private String remark;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID",example = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",example = "创建时间")
    @Column(name = "create_time")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
    * 修改人ID
    */
    @ApiModelProperty(value = "修改人ID",example = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",example = "修改时间")
    @Column(name = "modified_time")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifiedTime;

}