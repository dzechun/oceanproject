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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 条码规则关联集合表
 * base_barcode_rule_set_det
 * @author 18358
 * @date 2020-11-10 13:55:23
 */
@Data
@Table(name = "base_barcode_rule_set_det")
public class BaseBarcodeRuleSetDet extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 6848329023195946862L;
    /**
     * 条码规则关联集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetDetId",value = "条码规则关联集合ID")
    @Id
    @Column(name = "barcode_rule_set_det_id")
    @NotNull(groups = update.class,message = "条码规则关联集合ID不能为空")
    private Long barcodeRuleSetDetId;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
    @Excel(name = "条码规则集合ID", height = 20, width = 30)
    @Column(name = "barcode_rule_set_id")
    @NotNull(message = "条码规则集合ID不能为空")
    private Long barcodeRuleSetId;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Excel(name = "条码规则ID", height = 20, width = 30)
    @Column(name = "barcode_rule_id")
    @NotNull(message = "条码规则ID不能为空")
    private Long barcodeRuleId;

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
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30)
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30)
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30)
    private String option3;

}
