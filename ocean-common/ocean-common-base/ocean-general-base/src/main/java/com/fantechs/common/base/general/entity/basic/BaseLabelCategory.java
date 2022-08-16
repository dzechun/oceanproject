package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 标签类别信息
 * bcm_label_category
 * @author mr.lei
 * @date 2020-12-17 19:14:37
 */
@Data
@Table(name = "base_label_category")
public class BaseLabelCategory extends ValidGroup implements Serializable {
    /**
     * 标签类别id
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别id")
    @Id
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * 标签类别代码
     */
    @ApiModelProperty(name="labelCategoryCode",value = "标签类别代码")
    @Excel(name = "标签类别代码", height = 20, width = 30,orderNum="1")
    @Column(name = "label_category_code")
    private String labelCategoryCode;

    /**
     * 标签类别名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "标签类别名称")
    @Excel(name = "标签类别名称", height = 20, width = 30,orderNum="2")
    @Column(name = "label_category_name")
    private String labelCategoryName;

    /**
     * 描述
     */
    @ApiModelProperty(name="labelCategoryDesc",value = "描述")
    @Excel(name = "描述", height = 20, width = 30,orderNum="3")
    @Column(name = "label_category_desc")
    private String labelCategoryDesc;

    /**
     * 类型(0.SN,2.CSN)
     */
    @ApiModelProperty(name="barcodeType",value = "类型(0.SN,2.CSN)")
    @Excel(name = "类型(0.SN,2.CSN)", height = 20, width = 30,orderNum="3",replace = {"SN_0", "CSN_2"})
    @Column(name = "barcode_type")
    private Byte barcodeType;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="4",replace = {"无效_0", "有效_1"})
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

    private static final long serialVersionUID = 1L;
}