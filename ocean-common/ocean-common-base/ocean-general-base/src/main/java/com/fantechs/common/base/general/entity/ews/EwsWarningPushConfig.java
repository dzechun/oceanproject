package com.fantechs.common.base.general.entity.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigReWuiDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 预警推送配置
 * ews_warning_push_config
 * @author mr.lei
 * @date 2021-12-27 11:10:27
 */
@Data
@Table(name = "ews_warning_push_config")
public class EwsWarningPushConfig extends ValidGroup implements Serializable {
    /**
     * 预警推送配置ID
     */
    @ApiModelProperty(name="warningPushConfigId",value = "预警推送配置ID")
    @Excel(name = "预警推送配置ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "warning_push_config_id")
    private Long warningPushConfigId;

    /**
     * 预警事件ID
     */
    @ApiModelProperty(name="warningEventConfigId",value = "预警事件ID")
    @Excel(name = "预警事件ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_event_config_id")
    private Long warningEventConfigId;

    /**
     * 人员等级（0、一级人员 1、二级人员 2、三级人员）
     */
    @ApiModelProperty(name="personnelLevel",value = "人员等级（0、一级人员 1、二级人员 2、三级人员）")
    @Excel(name = "人员等级（0、一级人员 1、二级人员 2、三级人员）", height = 20, width = 30,orderNum="") 
    @Column(name = "personnel_level")
    private String personnelLevel;

    /**
     * 通知方式（0、微信 1、短信 2、钉钉 3、邮件）
     */
    @ApiModelProperty(name="notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）")
    @Excel(name = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）", height = 20, width = 30,orderNum="")
    @Column(name = "notification_method")
    private String notificationMethod;

    /**
     * 处理时间
     */
    @ApiModelProperty(name="handleTime",value = "处理时间")
    @Excel(name = "处理时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "handle_time")
    private Integer handleTime;

    /**
     * 处理时间单位
     */
    @ApiModelProperty(name="handleTimeUnit",value = "处理时间单位")
    @Excel(name = "处理时间单位", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "handle_time_unit")
    private String handleTimeUnit;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

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

    @Transient
    private List<EwsWarningPushConfigReWuiDto> ewsWarningPushConfigReWuiDtos;

    private static final long serialVersionUID = 1L;
}