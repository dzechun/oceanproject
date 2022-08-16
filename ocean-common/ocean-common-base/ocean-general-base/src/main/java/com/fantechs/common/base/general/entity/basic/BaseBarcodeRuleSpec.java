package com.fantechs.common.base.general.entity.basic;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * base_barcode_rule_spec
 * @author 18358
 * @date 2020-11-07 11:55:39
 */
@Data
@Table(name = "base_barcode_rule_spec")
public class BaseBarcodeRuleSpec extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 4652990118724195429L;
    /**
     * 条码规则配置ID
     */
    @ApiModelProperty(name="barcodeRuleSpecId",value = "条码规则配置ID")
    @Id
    @Column(name = "barcode_rule_spec_id")
    @NotNull(groups = update.class,message = "条码规则配置ID不能为空")
    private Long barcodeRuleSpecId;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Column(name = "barcode_rule_id")
    @NotNull(message = "条码规则ID不能为空")
    private Long barcodeRuleId;

    /**
     * 属性类别ID
     */
    @ApiModelProperty(name="specId",value = "属性类别ID")
    @Column(name = "spec_id")
    private Long specId;

    /**
     * 格式
     */
    @ApiModelProperty(name="specification",value = "格式")
    private String specification;

    /**
     * 长度
     */
    @ApiModelProperty(name="barcodeLength",value = "长度")
    private Integer barcodeLength;

    /**
     * 初始值
     */
    @ApiModelProperty(name="initialValue",value = "初始值")
    @Column(name = "initial_value")
    private Integer initialValue;

    /**
     * 步长
     */
    @ApiModelProperty(name="step",value = "步长")
    private Integer step;

    /**
     * 补位符
     */
    @ApiModelProperty(name="fillOperator",value = "补位符")
    @Column(name = "fill_operator")
    private String fillOperator;

    /**
     * 补位方向(0.前  1.后)
     */
    @ApiModelProperty(name="fillDirection",value = "补位方向(0.前  1.后)")
    @Column(name = "fill_direction")
    private Byte fillDirection;

    /**
     * 自定义函数名称
     */
    @ApiModelProperty(name="customizeName",value = "自定义函数名称")
    @Column(name = "customize_name")
    private String customizeName;

    /**
     * 截取长度
     */
    @ApiModelProperty(name="interceptLength",value = "截取长度")
    @Column(name = "intercept_length")
    private Integer interceptLength;

    /**
     * 截取位置
     */
    @ApiModelProperty(name="interceptPosition",value = "截取位置")
    @Column(name = "intercept_position")
    private Integer interceptPosition;

    /**
     * 截取方向(0.前  1.后)
     */
    @ApiModelProperty(name="interceptDirection",value = "截取方向(0.前  1.后)")
    @Column(name = "intercept_direction")
    private Byte interceptDirection;

    /**
     * 自定义参数值
     */
    @ApiModelProperty(name="customizeValue",value = "自定义参数值")
    @Column(name = "customize_value")
    private String customizeValue;

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

}
