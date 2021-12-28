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
 * 预警事件配置
 * ews_warning_event_config
 * @author mr.lei
 * @date 2021-12-27 11:10:27
 */
@Data
@Table(name = "ews_warning_event_config")
public class EwsWarningEventConfig extends ValidGroup implements Serializable {
    /**
     * 预警事件配置ID
     */
    @ApiModelProperty(name="warningEventConfigId",value = "预警事件配置ID")
    @Excel(name = "预警事件配置ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "warning_event_config_id")
    private Long warningEventConfigId;

    /**
     * 预警事件ID编码
     */
    @ApiModelProperty(name="warningEventIdCode",value = "预警事件ID编码")
    @Excel(name = "预警事件ID编码", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_event_id_code")
    private String warningEventIdCode;

    /**
     * 预警事件名称
     */
    @ApiModelProperty(name="warningEventName",value = "预警事件名称")
    @Excel(name = "预警事件名称", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_event_name")
    private String warningEventName;

    /**
     * 预警事件描述
     */
    @ApiModelProperty(name="warningEventDesc",value = "预警事件描述")
    @Excel(name = "预警事件描述", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_event_desc")
    private String warningEventDesc;

    /**
     * 对象名称
     */
    @ApiModelProperty(name="objectName",value = "对象名称")
    @Excel(name = "对象名称", height = 20, width = 30,orderNum="") 
    @Column(name = "object_name")
    private String objectName;

    /**
     * 对象类别
     */
    @ApiModelProperty(name="objectType",value = "对象类别")
    @Excel(name = "对象类别", height = 20, width = 30,orderNum="") 
    @Column(name = "object_type")
    private String objectType;

    /**
     * 传递参数
     */
    @ApiModelProperty(name="sendParameter",value = "传递参数")
    @Excel(name = "传递参数", height = 20, width = 30,orderNum="") 
    @Column(name = "send_parameter")
    private String sendParameter;

    /**
     * 调用方法(1-GET 2-POST)
     */
    @ApiModelProperty(name="callingMethod",value = "调用方法(1-GET 2-POST)")
    @Excel(name = "调用方法(1-GET 2-POST)", height = 20, width = 30,orderNum="") 
    @Column(name = "calling_method")
    private Byte callingMethod;

    /**
     * 地址
     */
    @ApiModelProperty(name="url",value = "地址")
    @Excel(name = "地址", height = 20, width = 30,orderNum="") 
    private String url;

    /**
     * 数据交互类别(1-读数据 2-写数据)
     */
    @ApiModelProperty(name="dataInteractionType",value = "数据交互类别(1-读数据 2-写数据)")
    @Excel(name = "数据交互类别(1-读数据 2-写数据)", height = 20, width = 30,orderNum="") 
    @Column(name = "data_interaction_type")
    private Byte dataInteractionType;

    /**
     * 间隔时间(s)
     */
    @ApiModelProperty(name="intervalTime",value = "间隔时间")
    @Excel(name = "间隔时间", height = 20, width = 30,orderNum="")
    @Column(name = "interval_time")
    private String intervalTime;

    /**
     * 执行状态(1-启动中 2-未启动)
     */
    @ApiModelProperty(name="executeStatus",value = "执行状态(1-启动中 2-未启动)")
    @Excel(name = "执行状态(1-启动中 2-未启动)", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_status")
    private Byte executeStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
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

    private String option1;

    private String option2;

    private String option3;



    private static final long serialVersionUID = 1L;
}