package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 出入库规则明细
 * base_in_and_out_rule_det
 * @author admin
 * @date 2021-05-14 16:28:21
 */
@Data
@Table(name = "base_in_and_out_rule_det")
public class BaseInAndOutRuleDet extends ValidGroup implements Serializable {
    /**
     * 出入库规则明细ID
     */
    @ApiModelProperty(name="inAndOutRuleDetId",value = "出入库规则明细ID")
    @Excel(name = "出入库规则明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "in_and_out_rule_det_id")
    private Long inAndOutRuleDetId;

    /**
     * 出入库规则ID
     */
    @ApiModelProperty(name="inAndOutRuleId",value = "出入库规则ID")
    @Excel(name = "出入库规则ID", height = 20, width = 30,orderNum="") 
    @Column(name = "in_and_out_rule_id")
    private Long inAndOutRuleId;

    /**
     * 出入库规则类型ID
     */
    @ApiModelProperty(name="inAndOutRuleTypeId",value = "出入库规则类型ID")
    @Excel(name = "出入库规则类型ID", height = 20, width = 30,orderNum="") 
    @Column(name = "in_and_out_rule_type_id")
    private Long inAndOutRuleTypeId;

    /**
     * 优先级
     */
    @ApiModelProperty(name="priority",value = "优先级")
    @Excel(name = "优先级", height = 20, width = 30,orderNum="") 
    private Integer priority;

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

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 出入库规则类型
     */
    @Transient
    @ApiModelProperty(name = "inAndOutRuleTypeName",value = "出入库规则类型")
    private String inAndOutRuleTypeName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}