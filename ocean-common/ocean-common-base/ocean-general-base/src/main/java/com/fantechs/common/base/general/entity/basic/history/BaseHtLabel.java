package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 标签历史信息
 * bcm_ht_label
 * @author mr.lei
 * @date 2020-12-17 13:47:29
 */
@Data
@Table(name = "base_ht_label")
public class BaseHtLabel extends ValidGroup implements Serializable {
    /**
     * 标签信息历史id
     */
    @ApiModelProperty(name="labelHtId",value = "标签信息历史id")
    @Excel(name = "标签信息历史id", height = 20, width = 30)
    @Id
    @Column(name = "label_ht_id")
    private Long labelHtId;

    /**
     * 标签信息id
     */
    @ApiModelProperty(name="labelId",value = "标签信息id")
    @Excel(name = "标签信息id", height = 20, width = 30)
    @Column(name = "label_id")
    private Long labelId;

    /**
     * 标签代码
     */
    @ApiModelProperty(name="labelCode",value = "标签代码")
    @Excel(name = "标签代码", height = 20, width = 30)
    @Column(name = "label_code")
    private String labelCode;

    /**
     * 标签名称
     */
    @ApiModelProperty(name="labelName",value = "标签名称")
    @Excel(name = "标签名称", height = 20, width = 30)
    @Column(name = "label_name")
    private String labelName;

    /**
     * 描述
     */
    @ApiModelProperty(name="labelDesc",value = "描述")
    @Excel(name = "描述", height = 20, width = 30)
    @Column(name = "label_desc")
    private String labelDesc;

    /**
     * 版本
     */
    @ApiModelProperty(name="labelVersion",value = "版本")
    @Excel(name = "版本", height = 20, width = 30)
    @Column(name = "label_version")
    private String labelVersion;

    /**
     * 打印方式
     */
    @ApiModelProperty(name="printMode",value = "打印方式")
    @Excel(name = "打印方式", height = 20, width = 30)
    @Column(name = "print_mode")
    private String printMode;

    /**
     * 标签类别id
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别id")
    @Excel(name = "标签类别id", height = 20, width = 30)
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * 存放路径
     */
    @ApiModelProperty(name="savePath",value = "存放路径")
    @Excel(name = "存放路径", height = 20, width = 30)
    @Column(name = "save_path")
    private String savePath;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
     * 是否默认模版（Y/N）
     */
    @ApiModelProperty(name="isDefaultLabel",value = "是否默认模版（Y/N）")
    @Column(name = "is_default_label")
    private Byte isDefaultLabel;

    private static final long serialVersionUID = 1L;
}