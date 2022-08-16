package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 条码规则集合表
 * base_barcode_rule_set
 * @author 18358
 * @date 2020-11-09 17:57:19
 */
@Data
@Table(name = "base_barcode_rule_set")
public class BaseBarcodeRuleSet extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 7548697567720143055L;
    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
    @Id
    @Column(name = "barcode_rule_set_id")
    @NotNull(groups = update.class,message = "条码规则集合ID不能为空")
    private Long barcodeRuleSetId;

    /**
     * 条码规则集合编码
     */
    @ApiModelProperty(name="barcodeRuleSetCode",value = "条码规则集合编码")
    @Excel(name = "条码规则集合编码", height = 20, width = 30,orderNum="1")
    @Column(name = "barcode_rule_set_code")
    @NotNull(message = "条码规则集合编码不能为空")
    private String barcodeRuleSetCode;

    /**
     * 条码规则集合名称
     */
    @ApiModelProperty(name="barcodeRuleSetName",value = "条码规则集合名称")
    @Excel(name = "条码规则集合名称", height = 20, width = 30,orderNum="2")
    @Column(name = "barcode_rule_set_name")
    @NotBlank(message = "条码规则集合名称不能为空")
    private String barcodeRuleSetName;

    /**
     * 条码规则集合描述
     */
    @ApiModelProperty(name="barcodeRuleSetDesc",value = "条码规则集合描述")
    @Excel(name = "条码规则集合描述", height = 20, width = 30,orderNum="3")
    @Column(name = "barcode_rule_set_desc")
    @NotBlank(message = "条码规则集合描述不能为空")
    private String barcodeRuleSetDesc;

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
    @Excel(name = "状态", height = 20, width = 30,orderNum="4",replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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

}
