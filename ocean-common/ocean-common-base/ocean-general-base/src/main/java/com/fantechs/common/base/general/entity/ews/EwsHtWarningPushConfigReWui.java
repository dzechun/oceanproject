package com.fantechs.common.base.general.entity.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
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

;
;

/**
 * 预警推送人员关系履历表(base_warning_user_info)
 * ews_ht_warning_push_config_re_wui
 * @author mr.lei
 * @date 2021-12-30 13:49:59
 */
@Data
@Table(name = "ews_ht_warning_push_config_re_wui")
public class EwsHtWarningPushConfigReWui extends ValidGroup implements Serializable {
    /**
     * 预警推送人员关系履历表ID
     */
    @ApiModelProperty(name="htWarningPushConfigReWuiId",value = "预警推送人员关系履历表ID")
    @Excel(name = "预警推送人员关系履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_warning_push_config_re_wui_id")
    private Long htWarningPushConfigReWuiId;

    /**
     * 预警推送人员关系表ID
     */
    @ApiModelProperty(name="warningPushConfigReWuiId",value = "预警推送人员关系表ID")
    @Excel(name = "预警推送人员关系表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_push_config_re_wui_id")
    private Long warningPushConfigReWuiId;

    /**
     * 预警推送配置表ID
     */
    @ApiModelProperty(name="warningPushConfigId",value = "预警推送配置表ID")
    @Excel(name = "预警推送配置表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_push_config_id")
    private Long warningPushConfigId;

    /**
     * 预警人员信息表ID
     */
    @ApiModelProperty(name="warningUserInfoId",value = "预警人员信息表ID")
    @Excel(name = "预警人员信息表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_user_info_id")
    private Long warningUserInfoId;

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

    private static final long serialVersionUID = 1L;
}