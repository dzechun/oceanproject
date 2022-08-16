package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 标签类别历史信息
 * bcm_ht_label_category
 * @author mr.lei
 * @date 2020-12-17 11:08:22
 */
@Data
@Table(name = "base_ht_label_category")
public class BaseHtLabelCategory implements Serializable {
    /**
     * 标签类别历史id
     */
    @ApiModelProperty(name="labelHtCategoryId",value = "标签类别历史id")
    @Excel(name = "标签类别历史id", height = 20, width = 30)
    @Id
    @Column(name = "label_ht_category_id")
    private Long labelHtCategoryId;

    /**
     * 标签类别id
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别id")
    @Excel(name = "标签类别id", height = 20, width = 30)
    @Column(name = "`label_category_id`")
    private Long labelCategoryId;

    /**
     * 标签类别代码
     */
    @ApiModelProperty(name="labelCategoryCode",value = "标签类别代码")
    @Excel(name = "标签类别代码", height = 20, width = 30)
    @Column(name = "label_category_code")
    private String labelCategoryCode;

    /**
     * 标签类别名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "标签类别名称")
    @Excel(name = "标签类别名称", height = 20, width = 30)
    @Column(name = "label_category_name")
    private String labelCategoryName;

    /**
     * 描述
     */
    @ApiModelProperty(name="labelCategoryDesc",value = "描述")
    @Excel(name = "描述", height = 20, width = 30)
    @Column(name = "label_category_desc")
    private String labelCategoryDesc;

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
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Byte status;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 类型(0.SN,2.CSN)
     */
    @ApiModelProperty(name="barcodeType",value = "类型(0.SN,2.CSN)")
    @Excel(name = "类型(0.SN,2.CSN)", height = 20, width = 30,orderNum="3")
    @Column(name = "barcode_type")
    private Byte barcodeType;

    private static final long serialVersionUID = 1L;
}