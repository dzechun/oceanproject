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

;
;

/**
 * 标签变量信息
 * base_label_variable
 * @author admin
 * @date 2021-09-16 15:20:13
 */
@Data
@Table(name = "base_label_variable")
public class BaseLabelVariable extends ValidGroup implements Serializable {
    /**
     * 标签变量ID
     */
    @ApiModelProperty(name="labelVariableId",value = "标签变量ID")
    @Id
    @Column(name = "label_variable_id")
    private Long labelVariableId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 标签类别ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别ID")
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * value1
     */
    @ApiModelProperty(name="value1",value = "value1")
    @Excel(name = "value1", height = 20, width = 30,orderNum="5")
    private String value1;

    /**
     * value2
     */
    @ApiModelProperty(name="value2",value = "value2")
    @Excel(name = "value2", height = 20, width = 30,orderNum="6")
    private String value2;

    /**
     * value3
     */
    @ApiModelProperty(name="value3",value = "value3")
    @Excel(name = "value3", height = 20, width = 30,orderNum="7")
    private String value3;

    /**
     * value4
     */
    @ApiModelProperty(name="value4",value = "value4")
    @Excel(name = "value4", height = 20, width = 30,orderNum="8")
    private String value4;

    /**
     * value5
     */
    @ApiModelProperty(name="value5",value = "value5")
    @Excel(name = "value5", height = 20, width = 30,orderNum="9")
    private String value5;

    /**
     * value6
     */
    @ApiModelProperty(name="value6",value = "value6")
    @Excel(name = "value6", height = 20, width = 30,orderNum="10")
    private String value6;

    /**
     * value7
     */
    @ApiModelProperty(name="value7",value = "value7")
    @Excel(name = "value7", height = 20, width = 30,orderNum="11")
    private String value7;

    /**
     * value8
     */
    @ApiModelProperty(name="value8",value = "value8")
    @Excel(name = "value8", height = 20, width = 30,orderNum="12")
    private String value8;

    /**
     * value9
     */
    @ApiModelProperty(name="value9",value = "value9")
    @Excel(name = "value9", height = 20, width = 30,orderNum="13")
    private String value9;

    /**
     * value10
     */
    @ApiModelProperty(name="value10",value = "value10")
    @Excel(name = "value10", height = 20, width = 30,orderNum="14")
    private String value10;

    /**
     * value11
     */
    @ApiModelProperty(name="value11",value = "value11")
    @Excel(name = "value11", height = 20, width = 30,orderNum="15")
    private String value11;

    /**
     * value12
     */
    @ApiModelProperty(name="value12",value = "value12")
    @Excel(name = "value12", height = 20, width = 30,orderNum="16")
    private String value12;

    /**
     * value13
     */
    @ApiModelProperty(name="value13",value = "value13")
    @Excel(name = "value13", height = 20, width = 30,orderNum="17")
    private String value13;

    /**
     * value14
     */
    @ApiModelProperty(name="value14",value = "value14")
    @Excel(name = "value14", height = 20, width = 30,orderNum="18")
    private String value14;

    /**
     * value15
     */
    @ApiModelProperty(name="value15",value = "value15")
    @Excel(name = "value15", height = 20, width = 30,orderNum="19")
    private String value15;

    /**
     * value16
     */
    @ApiModelProperty(name="value16",value = "value16")
    @Excel(name = "value16", height = 20, width = 30,orderNum="20")
    private String value16;

    /**
     * value17
     */
    @ApiModelProperty(name="value17",value = "value17")
    @Excel(name = "value17", height = 20, width = 30,orderNum="21")
    private String value17;

    /**
     * value18
     */
    @ApiModelProperty(name="value18",value = "value18")
    @Excel(name = "value18", height = 20, width = 30,orderNum="22")
    private String value18;

    /**
     * value19
     */
    @ApiModelProperty(name="value19",value = "value19")
    @Excel(name = "value19", height = 20, width = 30,orderNum="23")
    private String value19;

    /**
     * value20
     */
    @ApiModelProperty(name="value20",value = "value20")
    @Excel(name = "value20", height = 20, width = 30,orderNum="24")
    private String value20;

    /**
     * value21
     */
    @ApiModelProperty(name="value21",value = "value21")
    @Excel(name = "value21", height = 20, width = 30,orderNum="25")
    private String value21;

    /**
     * value22
     */
    @ApiModelProperty(name="value22",value = "value22")
    @Excel(name = "value22", height = 20, width = 30,orderNum="26")
    private String value22;

    /**
     * value23
     */
    @ApiModelProperty(name="value23",value = "value23")
    @Excel(name = "value23", height = 20, width = 30,orderNum="27")
    private String value23;

    /**
     * value24
     */
    @ApiModelProperty(name="value24",value = "value24")
    @Excel(name = "value24", height = 20, width = 30,orderNum="28")
    private String value24;

    /**
     * value25
     */
    @ApiModelProperty(name="value25",value = "value25")
    @Excel(name = "value25", height = 20, width = 30,orderNum="29")
    private String value25;

    /**
     * value26
     */
    @ApiModelProperty(name="value26",value = "value26")
    @Excel(name = "value26", height = 20, width = 30,orderNum="30")
    private String value26;

    /**
     * value27
     */
    @ApiModelProperty(name="value27",value = "value27")
    @Excel(name = "value27", height = 20, width = 30,orderNum="31")
    private String value27;

    /**
     * value28
     */
    @ApiModelProperty(name="value28",value = "value28")
    @Excel(name = "value28", height = 20, width = 30,orderNum="32")
    private String value28;

    /**
     * value29
     */
    @ApiModelProperty(name="value29",value = "value29")
    @Excel(name = "value29", height = 20, width = 30,orderNum="33")
    private String value29;

    /**
     * value30
     */
    @ApiModelProperty(name="value30",value = "value30")
    @Excel(name = "value30", height = 20, width = 30,orderNum="34")
    private String value30;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="36",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="38",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}