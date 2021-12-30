package com.fantechs.common.base.general.entity.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 执行事件日志
 * ews_warning_event_execute_log
 * @author mr.lei
 * @date 2021-12-28 10:08:19
 */
@Data
@Table(name = "ews_warning_event_execute_log")
public class EwsWarningEventExecuteLog extends ValidGroup implements Serializable {
    /**
     * 执行事件日志ID
     */
    @ApiModelProperty(name="warningEventExecuteLogId",value = "执行事件日志ID")
    @Excel(name = "执行事件日志ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "warning_event_execute_log_id")
    private Long warningEventExecuteLogId;

    /**
     * 预警事件配置ID
     */
    @ApiModelProperty(name="warningEventConfigId",value = "预警事件配置ID")
    @Excel(name = "预警事件配置ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_event_config_id")
    private Long warningEventConfigId;

    /**
     * 执行结果(1-成功 2-失败)
     */
    @ApiModelProperty(name="executeResult",value = "执行结果(1-成功 2-失败)")
    @Excel(name = "执行结果(1-成功 2-失败)", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_result")
    private Byte executeResult;

    /**
     * 处理结果(1-未确认 2-已确认 3-不予处理)
     */
    @ApiModelProperty(name="handleResult",value = "处理结果(1-未确认 2-已确认 3-不予处理)")
    @Excel(name = "处理结果(1-未确认 2-已确认 3-不予处理)", height = 20, width = 30,orderNum="") 
    @Column(name = "handle_result")
    private Byte handleResult;

    /**
     * 执行时间
     */
    @ApiModelProperty(name="executeTime",value = "执行时间")
    @Excel(name = "执行时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "execute_time")
    private Date executeTime;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

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
     * 逻辑删除(0-删除 1-正常)
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除(0-删除 1-正常)")
    @Excel(name = "逻辑删除(0-删除 1-正常)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 执行参数
     */
    @ApiModelProperty(name="executeParameter",value = "执行参数")
    @Excel(name = "执行参数", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_parameter")
    private String executeParameter;

    /**
     * 执行结果通知消息
     */
    @ApiModelProperty(name="executeResultMessage",value = "执行结果通知消息")
    @Excel(name = "执行结果通知消息", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_result_message")
    private String executeResultMessage;

    private static final long serialVersionUID = 1L;
}