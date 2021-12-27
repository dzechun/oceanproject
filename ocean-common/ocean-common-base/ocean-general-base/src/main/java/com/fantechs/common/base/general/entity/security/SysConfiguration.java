package com.fantechs.common.base.general.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 业务配置表
 * sys_configuration
 * @author lfz
 * @date 2021-12-09 11:04:57
 */
@Data
@Table(name = "sys_configuration")
public class SysConfiguration extends ValidGroup implements Serializable {
    /**
     * 配置Id
     */
    @ApiModelProperty(name="configurationId",value = "配置Id")
    @Excel(name = "配置Id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "configuration_id")
    private Long configurationId;

    /**
     * 配置名称
     */
    @ApiModelProperty(name="configurationName",value = "配置名称")
    @Excel(name = "配置名称", height = 20, width = 30,orderNum="") 
    @Column(name = "configuration_name")
    private String configurationName;

    /**
     * 源对象
     */
    @ApiModelProperty(name="configurationSource",value = "源对象")
    @Excel(name = "源对象", height = 20, width = 30,orderNum="") 
    @Column(name = "configuration_source")
    private String configurationSource;

    /**
     * 目标对象
     */
    @ApiModelProperty(name="configurationTarget",value = "目标对象")
    @Excel(name = "目标对象", height = 20, width = 30,orderNum="") 
    @Column(name = "configuration_target")
    private String configurationTarget;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
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
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

    private static final long serialVersionUID = 1L;
}