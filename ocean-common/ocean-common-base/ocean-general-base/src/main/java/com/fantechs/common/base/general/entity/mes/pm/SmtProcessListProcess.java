package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 流程单工序表
 * base_process_list_process
 * @author mr.lei
 * @date 2020-11-23 16:38:37
 */
@Data
@Table(name = "base_process_list_process")
public class SmtProcessListProcess extends ValidGroup implements Serializable {
    /**
     * 流程单工序ID
     */
    @ApiModelProperty(name="processListProcessId",value = "流程单工序ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Column(name = "process_list_process_id")
    private Long processListProcessId;

    /**
     * 流程单工序编码
     */
    @ApiModelProperty(name="processListProcessCode",value = "流程单工序编码")
    @Column(name = "process_list_process_code")
    @Excel(name = "过站单号", height = 20, width = 30,orderNum = "3")
    private String processListProcessCode;

    /**
     * 工单流程卡任务池ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "工单流程卡任务池ID")
    @Column(name = "work_order_card_pool_id")
    private Long workOrderCardPoolId;

    /**
     * 工单条码任务池ID
     */
    @ApiModelProperty(name="workOrderBarcodePoolId",value = "工单条码任务池ID")
    @Column(name = "work_order_barcode_pool_id")
    private Long workOrderBarcodePoolId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 工序操作类型（1、开工 2、报工）
     */
    @ApiModelProperty(name="process_type",value = "工序操作类型（1、开工 2、报工）")
    private Byte processType;

    /**
     * 员工ID
     */
    @ApiModelProperty(name="staffId",value = "员工ID")
    @Column(name = "staff_id")
    private Long staffId;

    /**
     * 过站状态(0-待开始 1-过站中 2-OK  3-NG)
     */
    @ApiModelProperty(name="status",value = "过站状态(0-待开始 1-过站中 2-OK  3-NG)")
    private Byte status;

    /**
     * 是否挂起(0-否 1-是)
     */
    @ApiModelProperty(name="isHold",value = "是否挂起(0-否 1-是)")
    @Column(name = "is_hold")
    private Byte isHold;

    /**
     * 入站时间
     */
    @ApiModelProperty(name="inboundTime",value = "入站时间")
    @Excel(name = "入站时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss",orderNum = "98")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "inbound_time")
    private Date inboundTime;

    /**
     * 出站时间
     */
    @ApiModelProperty(name="outboundTime",value = "出站时间")
    @Excel(name = "出站时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss",orderNum = "99")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "outbound_time")
    private Date outboundTime;

    /**
     * 开工数量
     */
    @ApiModelProperty(name="startWorkQty",value = "开工数量")
    @Excel(name = "开工数量", height = 20, width = 30,orderNum = "10")
    @Column(name = "start_work_qty")
    private BigDecimal startWorkQty;

    /**
     * 报工数量
     */
    @ApiModelProperty(name="outputQuantity",value = "报工数量")
    @Excel(name = "报工数量", height = 20, width = 30,orderNum = "11")
    @Column(name = "output_quantity")
    private BigDecimal outputQuantity;

    /**
     * 本次报工数量
     */
    @ApiModelProperty(name="curOutputQty",value = "本次报工数量")
    @Excel(name = "本次报工数量", height = 20, width = 30,orderNum = "12")
    @Column(name = "cur_output_qty")
    private BigDecimal curOutputQty;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="packageNum",value = "彩盒号")
    @Column(name = "package_num")
    private String packageNum;

    /**
     * 箱号
     */
    @ApiModelProperty(name="boxNum",value = "箱号")
    @Column(name = "box_num")
    private String boxNum;

    /**
     * 线板号
     */
    @ApiModelProperty(name="filamentPlateNum",value = "线板号")
    @Column(name = "filament_plate_num")
    private String filamentPlateNum;

    /**
     * 抽验单
     */
    @ApiModelProperty(name="selective",value = "抽验单")
    private String selective;

    /**
     * 抽验结果
     */
    @ApiModelProperty(name="selectiveResult",value = "抽验结果")
    @Column(name = "selective_result")
    private String selectiveResult;

    /**
     * 返工单Id
     */
    @ApiModelProperty(name="reworkId",value = "返工单Id")
    @Column(name = "rework_id")
    private Long reworkId;

    /**
     * 作业员
     */
    @ApiModelProperty(name="operator",value = "作业员")
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
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}
