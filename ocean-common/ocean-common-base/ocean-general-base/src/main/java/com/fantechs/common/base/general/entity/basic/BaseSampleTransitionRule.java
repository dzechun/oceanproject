package com.fantechs.common.base.general.entity.basic;

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
import java.util.List;

@Data
@Table(name = "base_sample_transition_rule")
public class BaseSampleTransitionRule extends ValidGroup implements Serializable {
    /**
     * 抽样转移规则ID
     */
    @ApiModelProperty(name="sampleTransitionRuleId",value = "抽样转移规则ID")
    @Id
    @Column(name = "sample_transition_rule_id")
    private Long sampleTransitionRuleId;

    /**
     * 抽样转移规则编码
     */
    @ApiModelProperty(name="sampleTransitionRuleCode",value = "抽样转移规则编码")
    @Excel(name = "抽样转移规则编码", height = 20, width = 30,orderNum="1")
    @Column(name = "sample_transition_rule_code")
    private String sampleTransitionRuleCode;

    /**
     * 抽样转移规则描述
     */
    @ApiModelProperty(name="sampleTransitionRuleDesc",value = "抽样转移规则描述")
    @Excel(name = "抽样转移规则描述", height = 20, width = 30,orderNum="2")
    @Column(name = "sample_transition_rule_desc")
    private String sampleTransitionRuleDesc;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="3")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 明细对象集合
     */
    @ApiModelProperty(name="list",value = "明细对象集合")
    private List<BaseSampleTransitionRuleDet> list;

    private static final long serialVersionUID = 1L;
}
