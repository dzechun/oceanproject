package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
public class BaseSampleTransitionRuleDet extends ValidGroup implements Serializable {
    /**
     * 抽样转移规则明细ID
     */
    @ApiModelProperty(name="sampleTransitionRuleDetId",value = "抽样转移规则明细ID")
    @Excel(name = "抽样转移规则明细ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "sample_transition_rule_det_id")
    private Long sampleTransitionRuleDetId;

    /**
     * 抽样转移规则ID
     */
    @ApiModelProperty(name="sampleTransitionRuleId",value = "抽样转移规则ID")
    @Excel(name = "抽样转移规则ID", height = 20, width = 30,orderNum="")
    @Column(name = "sample_transition_rule_id")
    private Long sampleTransitionRuleId;

    /**
     * 是否初始阶段(0-否 1-是)
     */
    @ApiModelProperty(name="ifInitialPhase",value = "是否初始阶段(0-否 1-是)")
    @Excel(name = "是否初始阶段(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "if_initial_phase")
    private Byte ifInitialPhase;

    /**
     * 严格度阶段(1-正常到加严 2-正常到放宽 3-加严到正常 )
     */
    @ApiModelProperty(name="rigorStage",value = "严格度阶段(1-正常到加严 2-正常到放宽 3-加严到正常 )")
    @Excel(name = "严格度阶段(1-正常到加严 2-正常到放宽 3-加严到正常 )", height = 20, width = 30,orderNum="")
    @Column(name = "rigor_stage")
    private Byte rigorStage;

    /**
     * 连续批次条件(1-小于 2-等于 3-大于)
     */
    @ApiModelProperty(name="continuousBatchCondition",value = "连续批次条件(1-小于 2-等于 3-大于)")
    @Excel(name = "连续批次条件(1-小于 2-等于 3-大于)", height = 20, width = 30,orderNum="")
    @Column(name = "continuous_batch_condition")
    private Byte continuousBatchCondition;

    /**
     * 连续批次数量
     */
    @ApiModelProperty(name="continuousBatchCount",value = "连续批次数量")
    @Excel(name = "连续批次数量", height = 20, width = 30,orderNum="")
    @Column(name = "continuous_batch_count")
    private Integer continuousBatchCount;

    /**
     * 接收批次数量
     */
    @ApiModelProperty(name="receiveBatchCount",value = "接收批次数量")
    @Excel(name = "接收批次数量", height = 20, width = 30,orderNum="")
    @Column(name = "receive_batch_count")
    private Integer receiveBatchCount;

    /**
     * 拒收批次数量
     */
    @ApiModelProperty(name="rejectionBatchCount",value = "拒收批次数量")
    @Excel(name = "拒收批次数量", height = 20, width = 30,orderNum="")
    @Column(name = "rejection_batch_count")
    private Integer rejectionBatchCount;

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

    private static final long serialVersionUID = 1L;
}
