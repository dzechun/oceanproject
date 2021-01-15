package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2020/11/23
 */
@Data
public class SmtProcessListProcessDto extends SmtProcessListProcess implements Serializable {
    private static final long serialVersionUID = -8014622703067473837L;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 任务单号
     */
    @ApiModelProperty(name="taskCode",value = "任务单号")
    @Excel(name = "任务单号", height = 20, width = 30,orderNum="")
    @Column(name = "task_code")
    private String taskCode;

    /**
     * 任务单号
     */
    @ApiModelProperty(name="barcodeTaskCode",value = "条码任务单号")
    @Excel(name = "条码任务单号", height = 20, width = 30,orderNum="")
    @Column(name = "task_code")
    private String barcodeTaskCode;

    /**
     * 物料ID
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料料号")
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    /**
     * 工艺路线ID
     */
    @Transient
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="procesName" ,value="工序名称")
    private String procesName;


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
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
