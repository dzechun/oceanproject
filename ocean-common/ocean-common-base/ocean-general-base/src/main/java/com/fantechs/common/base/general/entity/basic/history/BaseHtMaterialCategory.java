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
 * 物料类别历史表
 * @date 2020-12-31 10:00:53
 */
@Data
@Table(name = "base_ht_material_category")
public class BaseHtMaterialCategory implements Serializable {
    /**
     * 物料类别历史ID
     */
    @ApiModelProperty(name="htMaterialCategoryId",value = "物料类别历史ID")
    @Excel(name = "物料类别历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_material_category_id")
    private Long htMaterialCategoryId;

    /**
     * 物料类别ID
     */
    @ApiModelProperty(name="materialCategoryId",value = "物料类别ID")
    @Excel(name = "物料类别ID", height = 20, width = 30)
    @Column(name = "material_category_id")
    private Long materialCategoryId;

    /**
     * 物料类别编码
     */
    @ApiModelProperty(name="materialCategoryCode",value = "物料类别编码")
    @Excel(name = "物料类别编码", height = 20, width = 30)
    @Column(name = "material_category_code")
    private String materialCategoryCode;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(name="materialCategoryName",value = "物料类别名称")
    @Excel(name = "物料类别名称", height = 20, width = 30)
    @Column(name = "material_category_name")
    private String materialCategoryName;

    /**
     * 物料类别描述
     */
    @ApiModelProperty(name="materialCategoryDesc",value = "物料类别描述")
    @Excel(name = "物料类别描述", height = 20, width = 30)
    @Column(name = "material_category_desc")
    private String materialCategoryDesc;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    @Excel(name = "父级ID", height = 20, width = 30)
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
