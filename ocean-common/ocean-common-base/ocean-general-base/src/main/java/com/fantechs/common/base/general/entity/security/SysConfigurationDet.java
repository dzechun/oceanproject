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
 * 业务明细表
 * sys_configuration_det
 * @author lfz
 * @date 2021-12-09 10:59:19
 */
@Data
@Table(name = "sys_configuration_det")
public class SysConfigurationDet extends ValidGroup implements Serializable {
    /**
     * 配置明细Id
     */
    @ApiModelProperty(name="configurationDetId",value = "配置明细Id")
    @Excel(name = "配置明细Id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "configuration_det_id")
    private Long configurationDetId;

    /**
     * 配置Id
     */
    @ApiModelProperty(name="configurationId",value = "配置Id")
    @Excel(name = "配置Id", height = 20, width = 30,orderNum="") 
    @Column(name = "configuration_id")
    private Long configurationId;

    /**
     * 源字段
     */
    @ApiModelProperty(name="sourceField",value = "源字段")
    @Excel(name = "源字段", height = 20, width = 30,orderNum="") 
    @Column(name = "source_field")
    private String sourceField;

    /**
     * 目标字段
     */
    @ApiModelProperty(name="targetField",value = "目标字段")
    @Excel(name = "目标字段", height = 20, width = 30,orderNum="") 
    @Column(name = "target_field")
    private String targetField;

    /**
     * 条件范围（0、且 1、或）
     */
    @ApiModelProperty(name="isCondition",value = "条件范围（0、且 1、或）")
    @Excel(name = "条件范围（0、且 1、或）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_condition")
    private Byte isCondition;

    /**
     * 过滤范围（0-等于 1-不等于 2-大于  3-大于等于 4-小于 5-小于等于 6-为空  7-不为空 8-包含 9-不包含 10-模糊）
     */
    @ApiModelProperty(name="isScope",value = "过滤范围（0-等于 1-不等于 2-大于  3-大于等于 4-小于 5-小于等于 6-为空  7-不为空 8-包含 9-不包含 10-模糊）")
    @Excel(name = "过滤范围（0-等于 1-不等于 2-大于  3-大于等于 4-小于 5-小于等于 6-为空  7-不为空 8-包含 9-不包含 10-模糊）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_scope")
    private Byte isScope;

    /**
     * 过滤值
     */
    @ApiModelProperty(name="filterValue",value = "过滤值")
    @Excel(name = "过滤值", height = 20, width = 30,orderNum="") 
    @Column(name = "filter_value")
    private String filterValue;

    /**
     * 固定值
     */
    @ApiModelProperty(name="fixedValue",value = "固定值")
    @Excel(name = "固定值", height = 20, width = 30,orderNum="") 
    @Column(name = "fixed_value")
    private String fixedValue;

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

    private static final long serialVersionUID = 1L;
}