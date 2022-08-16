package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;



/**
 * 条码规则集合表
 * base_ht_barcode_rule_set
 * @author 18358
 * @date 2020-11-09 18:17:20
 */
@Data
@Table(name = "base_ht_barcode_rule_set")
public class BaseHtBarcodeRuleSet implements Serializable {
    private static final long serialVersionUID = -6782495515910874604L;
    /**
     * 条码规则集合历史ID
     */
    @ApiModelProperty(name="htBarcodeRuleSetId",value = "条码规则集合历史ID")
    @Id
    @Column(name = "ht_barcode_rule_set_id")
    private Long htBarcodeRuleSetId;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
    @Column(name = "barcode_rule_set_id")
    private Long barcodeRuleSetId;

    /**
     * 条码规则集合编码
     */
    @ApiModelProperty(name="barcodeRuleSetCode",value = "条码规则集合编码")
    @Column(name = "barcode_rule_set_code")
    private String barcodeRuleSetCode;

    /**
     * 条码规则集合名称
     */
    @ApiModelProperty(name="barcodeRuleSetName",value = "条码规则集合名称")
    @Column(name = "barcode_rule_set_name")
    private String barcodeRuleSetName;

    /**
     * 条码规则集合描述
     */
    @ApiModelProperty(name="barcodeRuleSetDesc",value = "条码规则集合描述")
    @Column(name = "barcode_rule_set_desc")
    private String barcodeRuleSetDesc;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

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
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

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
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

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
