package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 条码规则表
 * base_barcode_rule
 * @author wcz
 * @date 2020-10-26 14:09:43
 */
@Data
@Table(name = "base_barcode_rule")
public class BaseBarcodeRule extends ValidGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Id
    @Column(name = "barcode_rule_id")
    @NotNull(groups =update.class,message = "条码规则ID不能为空")
    private Long barcodeRuleId;

    /**
     * 条码规则代码
     */
    @ApiModelProperty(name="barcodeRuleCode",value = "条码规则代码")
    @Excel(name = "条码规则代码", height = 20, width = 30,orderNum="1")
    @Column(name = "barcode_rule_code")
    @NotBlank(message = "条码规则代码不能为空")
    private String barcodeRuleCode;

    /**
     * 条码规则名称
     */
    @ApiModelProperty(name="barcodeRuleName",value = "条码规则名称")
    @Excel(name = "条码规则名称", height = 20, width = 30,orderNum="2")
    @Column(name = "barcode_rule_name")
    @NotBlank(message = "条码规则名称不能为空")
    private String barcodeRuleName;

    /**
     * 条码规则描述
     */
    @ApiModelProperty(name="barcodeRuleDesc",value = "条码规则描述")
    @Excel(name = "条码规则描述", height = 20, width = 30,orderNum="3")
    @Column(name = "barcode_rule_desc")
    private String barcodeRuleDesc;

    /**
     * 条码规则类别ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "条码规则类别ID")
    @Column(name = "barcode_rule_category_id")
    @NotNull(message = "条码规则类别ID不能为空")
    private Long labelCategoryId;

    /**
     * 条码规则
     */
    @ApiModelProperty(name="barcodeRule",value = "条码规则")
    @Excel(name = "条码规则", height = 20, width = 30,orderNum="5")
    @Column(name = "barcode_rule")
    private String barcodeRule;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;


    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态", height = 20, width = 30,orderNum="6",replace = {"无效_0", "有效_1"})
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    @Transient
    private List<BaseBarcodeRuleSpec> barcodeRuleSpecs;
}
