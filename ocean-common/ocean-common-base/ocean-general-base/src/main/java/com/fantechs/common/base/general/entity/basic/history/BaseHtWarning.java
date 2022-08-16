package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 预警履历信息表
 * base_ht_warning
 * @author 53203
 * @date 2021-03-03 15:08:34
 */
@Data
@Table(name = "base_ht_warning")
public class BaseHtWarning extends ValidGroup implements Serializable {
    /**
     * 预警履历ID
     */
    @ApiModelProperty(name="htWarningId",value = "预警履历ID")
    @Excel(name = "预警履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_warning_id")
    private Long htWarningId;

    /**
     * 预警ID
     */
    @ApiModelProperty(name="warningId",value = "预警ID")
    @Excel(name = "预警ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_id")
    private Long warningId;

    /**
     * 预警类型
     */
    @ApiModelProperty(name="warningType",value = "预警类型")
    @Excel(name = "预警类型", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_type")
    private Long warningType;

    /**
     * 人员等级（0、一级人员 1、二级人员 2、三级人员）
     */
    @ApiModelProperty(name="personnelLevel",value = "人员等级（0、一级人员 1、二级人员 2、三级人员）")
    @Excel(name = "人员等级（0、一级人员 1、二级人员 2、三级人员）", height = 20, width = 30,orderNum="") 
    @Column(name = "personnel_level")
    private Byte personnelLevel;

    /**
     * 通知方式（0、微信 1、短信 2、钉钉）
     */
    @ApiModelProperty(name="notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉）")
    @Excel(name = "通知方式（0、微信 1、短信 2、钉钉）", height = 20, width = 30,orderNum="") 
    @Column(name = "notification_method")
    private Byte notificationMethod;

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
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long organizationId;

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
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    @Transient
    private Long organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}