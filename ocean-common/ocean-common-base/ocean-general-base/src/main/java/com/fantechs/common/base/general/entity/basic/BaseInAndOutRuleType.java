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
 * 出入库规则类型
 * base_in_and_out_rule_type
 * @author admin
 * @date 2021-05-14 15:44:36
 */
@Data
@Table(name = "base_in_and_out_rule_type")
public class BaseInAndOutRuleType extends ValidGroup implements Serializable {
    /**
     * 出入库规则类型ID
     */
    @ApiModelProperty(name="inAndOutRuleTypeId",value = "出入库规则类型ID")
    @Id
    @Column(name = "in_and_out_rule_type_id")
    private Long inAndOutRuleTypeId;

    /**
     * 出入库规则类型编码
     */
    @ApiModelProperty(name="inAndOutRuleTypeCode",value = "出入库规则类型编码")
    @Excel(name = "出入库规则类型编码", height = 20, width = 30,orderNum="1")
    @Column(name = "in_and_out_rule_type_code")
    private String inAndOutRuleTypeCode;

    /**
     * 出入库规则类型名称
     */
    @ApiModelProperty(name="inAndOutRuleTypeName",value = "出入库规则类型名称")
    @Excel(name = "出入库规则类型名称", height = 20, width = 30,orderNum="2")
    @Column(name = "in_and_out_rule_type_name")
    private String inAndOutRuleTypeName;

    /**
     * 类型(1-入库 2-出库)
     */
    @ApiModelProperty(name="category",value = "类型(1-入库 2-出库)")
    @Excel(name = "类型(1-入库 2-出库)", height = 20, width = 30,orderNum="3",replace = {"入库_1", "出库_2"})
    private Byte category;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="4",replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
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

    /**c
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;
}