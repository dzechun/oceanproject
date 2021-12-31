package com.fantechs.common.base.general.entity.basic;

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
 * 出入库规则明细履历表
 * base_ht_in_and_out_rule_det
 * @author mr.lei
 * @date 2021-12-30 19:30:23
 */
@Data
@Table(name = "base_ht_in_and_out_rule_det")
public class BaseHtInAndOutRuleDet extends ValidGroup implements Serializable {
    /**
     * 出入库规则明细履历ID
     */
    @ApiModelProperty(name="htInAndOutRuleDetId",value = "出入库规则明细履历ID")
    @Excel(name = "出入库规则明细履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_in_and_out_rule_det_id")
    private Long htInAndOutRuleDetId;

    /**
     * 出入库规则明细ID
     */
    @ApiModelProperty(name="inAndOutRuleDetId",value = "出入库规则明细ID")
    @Excel(name = "出入库规则明细ID", height = 20, width = 30,orderNum="") 
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
     * 优先级
     */
    @ApiModelProperty(name="priority",value = "优先级")
    @Excel(name = "优先级", height = 20, width = 30,orderNum="") 
    private Integer priority;

    /**
     * 规则名称(做成字典,枚举)
     */
    @ApiModelProperty(name="inAndOutRuleType",value = "规则名称(做成字典,枚举)")
    @Excel(name = "规则名称(做成字典,枚举)", height = 20, width = 30,orderNum="") 
    @Column(name = "in_and_out_rule_type")
    private Byte inAndOutRuleType;

    /**
     * 存储过程名称
     */
    @ApiModelProperty(name="storedProcedureName",value = "存储过程名称")
    @Excel(name = "存储过程名称", height = 20, width = 30,orderNum="") 
    @Column(name = "stored_procedure_name")
    private String storedProcedureName;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 入参
     */
    @ApiModelProperty(name="inParameters",value = "入参")
    @Excel(name = "入参", height = 20, width = 30,orderNum="") 
    @Column(name = "in_parameters")
    private String inParameters;

    /**
     * 出参
     */
    @ApiModelProperty(name="outParameters",value = "出参")
    @Excel(name = "出参", height = 20, width = 30,orderNum="") 
    @Column(name = "out_parameters")
    private String outParameters;

    private static final long serialVersionUID = 1L;
}