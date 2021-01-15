package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 流程单工序表
 * smt_process_list_process
 * @author mr.lei
 * @date 2020-11-23 16:38:37
 */
@Data
@Table(name = "smt_process_list_process")
public class SmtProcessListProcess extends ValidGroup implements Serializable {
    /**
     * 流程单工序ID
     */
    @ApiModelProperty(name="processListProcessId",value = "流程单工序ID")
    @Excel(name = "流程单工序ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "process_list_process_id")
    private Long processListProcessId;

    /**
     * 工单流程卡任务池ID
     */
    @ApiModelProperty(name="workOrderCardPooId",value = "工单流程卡任务池ID")
    @Excel(name = "工单流程卡任务池ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_card_poo_id")
    private Long workOrderCardPooId;

    /**
     * 工单条码任务池ID
     */
    @ApiModelProperty(name="workOrderBarcodePoolId",value = "工单条码任务池ID")
    @Excel(name = "工单条码任务池ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_barcode_pool_id")
    private Long workOrderBarcodePoolId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 过站状态(0-待开始 1-过站中 2-OK  3-NG)
     */
    @ApiModelProperty(name="status",value = "过站状态(0-待开始 1-过站中 2-OK  3-NG)")
    @Excel(name = "过站状态(0-待开始 1-过站中 2-OK  3-NG)", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 是否挂起(0-否 1-是)
     */
    @ApiModelProperty(name="isHold",value = "是否挂起(0-否 1-是)")
    @Excel(name = "是否挂起(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "is_hold")
    private Byte isHold;

    /**
     * 入站时间
     */
    @ApiModelProperty(name="inboundTime",value = "入站时间")
    @Excel(name = "入站时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "inbound_time")
    private Date inboundTime;

    /**
     * 出站时间
     */
    @ApiModelProperty(name="outboundTime",value = "出站时间")
    @Excel(name = "出站时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "outbound_time")
    private Date outboundTime;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="packageNum",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="")
    @Column(name = "package_num")
    private String packageNum;

    /**
     * 箱号
     */
    @ApiModelProperty(name="boxNum",value = "箱号")
    @Excel(name = "箱号", height = 20, width = 30,orderNum="")
    @Column(name = "box_num")
    private String boxNum;

    /**
     * 线板号
     */
    @ApiModelProperty(name="filamentPlateNum",value = "线板号")
    @Excel(name = "线板号", height = 20, width = 30,orderNum="")
    @Column(name = "filament_plate_num")
    private String filamentPlateNum;

    /**
     * 抽验单
     */
    @ApiModelProperty(name="selective",value = "抽验单")
    @Excel(name = "抽验单", height = 20, width = 30,orderNum="")
    private String selective;

    /**
     * 抽验结果
     */
    @ApiModelProperty(name="selectiveResult",value = "抽验结果")
    @Excel(name = "抽验结果", height = 20, width = 30,orderNum="")
    @Column(name = "selective_result")
    private String selectiveResult;

    /**
     * 返工单Id
     */
    @ApiModelProperty(name="reworkId",value = "返工单Id")
    @Excel(name = "返工单Id", height = 20, width = 30,orderNum="")
    @Column(name = "rework_id")
    private Long reworkId;

    /**
     * 作业员
     */
    @ApiModelProperty(name="operator",value = "作业员")
    @Excel(name = "作业员", height = 20, width = 30,orderNum="")
    private Long operator;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;


    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="")
    private String option3;

    private static final long serialVersionUID = 1L;
}
