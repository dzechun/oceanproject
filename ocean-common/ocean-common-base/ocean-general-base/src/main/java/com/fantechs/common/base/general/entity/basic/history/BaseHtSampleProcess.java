package com.fantechs.common.base.general.entity.basic.history;

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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 抽样过程履历表
 * base_ht_sample_process
 * @author admin
 * @date 2021-05-19 09:24:41
 */
@Data
@Table(name = "base_ht_sample_process")
public class BaseHtSampleProcess extends ValidGroup implements Serializable {
    /**
     * 抽样过程履历ID
     */
    @ApiModelProperty(name="htSampleProcessId",value = "抽样过程履历ID")
    @Excel(name = "抽样过程履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_sample_process_id")
    private Long htSampleProcessId;

    /**
     * 抽样过程ID
     */
    @ApiModelProperty(name="sampleProcessId",value = "抽样过程ID")
    @Excel(name = "抽样过程ID", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_process_id")
    private Long sampleProcessId;

    /**
     * 抽样过程编码
     */
    @ApiModelProperty(name="sampleProcessCode",value = "抽样过程编码")
    @Excel(name = "抽样过程编码", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_process_code")
    private String sampleProcessCode;

    /**
     * 抽样标准描述
     */
    @ApiModelProperty(name="sampleProcessDesc",value = "抽样标准描述")
    @Excel(name = "抽样标准描述", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_process_desc")
    private String sampleProcessDesc;

    /**
     * 抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)
     */
    @ApiModelProperty(name="sampleProcessType",value = "抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)")
    @Excel(name = "抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_process_type")
    private Byte sampleProcessType;

    /**
     * 抽样方案ID
     */
    @ApiModelProperty(name="samplePlanId",value = "抽样方案ID")
    @Excel(name = "抽样方案ID", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_plan_id")
    private Long samplePlanId;

    /**
     * 抽样方案AQL值表ID
     */
    @ApiModelProperty(name="samplePlanAqlId",value = "抽样方案AQL值表ID")
    @Column(name = "sample_plan_aql_id")
    private Long samplePlanAqlId;

    /**
     * 抽样转移规则ID
     */
    @ApiModelProperty(name="sampleTransitionRuleId",value = "抽样转移规则ID")
    @Excel(name = "抽样转移规则ID", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_transition_rule_id")
    private Long sampleTransitionRuleId;

    /**
     * 样本数
     */
    @ApiModelProperty(name="sampleQty",value = "样本数")
    @Excel(name = "样本数", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_qty")
    private BigDecimal sampleQty;

    /**
     * 抽样百分比
     */
    @ApiModelProperty(name="samplePercent",value = "抽样百分比")
    @Excel(name = "抽样百分比", height = 20, width = 30,orderNum="") 
    @Column(name = "sample_percent")
    private BigDecimal samplePercent;

    /**
     * AC值
     */
    @ApiModelProperty(name="acValue",value = "AC值")
    @Excel(name = "AC值", height = 20, width = 30,orderNum="") 
    @Column(name = "ac_value")
    private Integer acValue;

    /**
     * RE值
     */
    @ApiModelProperty(name="reValue",value = "RE值")
    @Excel(name = "RE值", height = 20, width = 30,orderNum="") 
    @Column(name = "re_value")
    private Integer reValue;

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
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="15")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="13")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 抽样方案
     */
    @ApiModelProperty(name="samplePlanCode" ,value="抽样方案")
    @Transient
    @Excel(name = "抽样方案", height = 20, width = 30,orderNum="4")
    private String samplePlanCode;

    /**
     * 抽样转移规则
     */
    @ApiModelProperty(name="sampleTransitionRuleCode" ,value="抽样转移规则")
    @Transient
    @Excel(name = "抽样转移规则", height = 20, width = 30,orderNum="7")
    private String sampleTransitionRuleCode;

    /**
     * AQL值
     */
    @ApiModelProperty(name="aqlValue" ,value="AQL值")
    @Transient
    @Excel(name = "AQL值", height = 20, width = 30,orderNum="6")
    private String aqlValue;

    private static final long serialVersionUID = 1L;
}