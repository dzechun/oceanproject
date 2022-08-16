package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BaseSampleTransitionRuleImport implements Serializable {

    /**
     * 抽样转移规则编码
     */
    @ApiModelProperty(name="sampleTransitionRuleCode" ,value="抽样转移规则编码")
    @Excel(name = "抽样转移规则编码(必填)", height = 20, width = 30)
    private String sampleTransitionRuleCode;

    /**
     * 抽样转移规则描述
     */
    @ApiModelProperty(name="sampleTransitionRuleDesc" ,value="抽样转移规则描述")
    @Excel(name = "抽样转移规则描述", height = 20, width = 30)
    private String sampleTransitionRuleDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;


    /**
     * 是否初始阶段(0-否 1-是)
     */
    @ApiModelProperty(name="ifInitialPhase",value = "是否初始阶段(0-否 1-是)")
    @Excel(name = "是否初始阶段(0-否 1-是)", height = 20, width = 30)
    private Integer ifInitialPhase;

    /**
     * 严格度阶段(1-正常到加严 2-正常到放宽 3-加严到正常)
     */
    @ApiModelProperty(name="rigorStage",value = "严格度阶段(1-正常到加严 2-正常到放宽 3-加严到正常)")
    @Excel(name = "严格度阶段(1-正常到加严 2-正常到放宽 3-加严到正常)", height = 20, width = 30)
    private Integer rigorStage;

    /**
     * 连续批次条件(1-小于 2-等于 3-大于)
     */
    @ApiModelProperty(name="continuousBatchCondition",value = "连续批次条件(1-小于 2-等于 3-大于)")
    @Excel(name = "连续批次条件(1-小于 2-等于 3-大于)", height = 20, width = 30)
    private Integer continuousBatchCondition;

    /**
     * 连续批次数量
     */
    @ApiModelProperty(name="continuousBatchCount",value = "连续批次数量")
    @Excel(name = "连续批次数量", height = 20, width = 30)
    private Integer continuousBatchCount;

    /**
     * 接收批次数量
     */
    @ApiModelProperty(name="receiveBatchCount",value = "接收批次数量")
    @Excel(name = "接收批次数量", height = 20, width = 30)
    private Integer receiveBatchCount;

    /**
     * 拒收批次数量
     */
    @ApiModelProperty(name="rejectionBatchCount",value = "拒收批次数量")
    @Excel(name = "拒收批次数量", height = 20, width = 30)
    private Integer rejectionBatchCount;
}
