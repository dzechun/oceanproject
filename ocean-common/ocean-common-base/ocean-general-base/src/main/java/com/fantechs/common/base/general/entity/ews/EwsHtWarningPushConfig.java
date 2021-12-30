package com.fantechs.common.base.general.entity.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.ews.EwsHtWarningPushConfigReWuiDto;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 预警推送配置履历表
 * ews_ht_warning_push_config
 * @author mr.lei
 * @date 2021-12-30 13:49:59
 */
@Data
@Table(name = "ews_ht_warning_push_config")
public class EwsHtWarningPushConfig extends ValidGroup implements Serializable {
    /**
     * 预警推送配置履历ID
     */
    @ApiModelProperty(name="htWarningPushConfigId",value = "预警推送配置履历ID")
    @Excel(name = "预警推送配置履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_warning_push_config_id")
    private Long htWarningPushConfigId;

    /**
     * 预警推送配置ID
     */
    @ApiModelProperty(name="warningPushConfigId",value = "预警推送配置ID")
    @Excel(name = "预警推送配置ID", height = 20, width = 30,orderNum="") 
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
     * 人员等级（1、一级人员 2、二级人员 3、三级人员）
     */
    @ApiModelProperty(name="personnelLevel",value = "人员等级（1、一级人员 2、二级人员 3、三级人员）")
    @Excel(name = "人员等级（1、一级人员 2、二级人员 3、三级人员）", height = 20, width = 30,orderNum="") 
    @Column(name = "personnel_level")
    private Byte personnelLevel;

    /**
     * 通知方式（1、微信 2、短信 3、钉钉）
     */
    @ApiModelProperty(name="notificationMethod",value = "通知方式（1、微信 2、短信 3、钉钉）")
    @Excel(name = "通知方式（1、微信 2、短信 3、钉钉）", height = 20, width = 30,orderNum="") 
    @Column(name = "notification_method")
    private String notificationMethod;

    /**
     * 处理时间
     */
    @ApiModelProperty(name="handleTime",value = "处理时间")
    @Excel(name = "处理时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "handle_time")
    private Integer handleTime;

    /**
     * 处理时间单位
     */
    @ApiModelProperty(name="handleTimeUnit",value = "处理时间单位")
    @Excel(name = "处理时间单位", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除(0-删除 1-正常)
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除(0-删除 1-正常)")
    @Excel(name = "逻辑删除(0-删除 1-正常)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private List<EwsHtWarningPushConfigReWuiDto> ewsHtWarningPushConfigReWuiDtoList;

    private static final long serialVersionUID = 1L;
}