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
import java.util.List;

/**
 * 物料类别表
 * @date 2020-12-31 09:49:58
 */
@Data
@Table(name = "base_material_category")
public class BaseMaterialCategory extends ValidGroup implements Serializable {
    /**
     * 物料类别ID
     */
    @ApiModelProperty(name="materialCategoryId",value = "物料类别ID")
    @Id
    @Column(name = "material_category_id")
    private Long materialCategoryId;

    /**
     * 物料类别编码
     */
    @ApiModelProperty(name="materialCategoryCode",value = "物料类别编码")
    @Excel(name = "物料类别编码", height = 20, width = 30,orderNum="1")
    @Column(name = "material_category_code")
    private String materialCategoryCode;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(name="materialCategoryName",value = "物料类别名称")
    @Excel(name = "物料类别名称", height = 20, width = 30,orderNum="2")
    @Column(name = "material_category_name")
    private String materialCategoryName;

    /**
     * 物料类别描述
     */
    @ApiModelProperty(name="materialCategoryDesc",value = "物料类别描述")
    @Excel(name = "物料类别描述", height = 20, width = 30,orderNum="3")
    @Column(name = "material_category_desc")
    private String materialCategoryDesc;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    @Excel(name = "父级ID", height = 20, width = 30,orderNum="4")
    @Column(name = "parent_id")
    private Long parentId;

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
    @Excel(name = "备注", height = 20, width = 30,orderNum="6")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="5",replace = {"无效_0","有效_1"})
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private List<BaseMaterialCategory> list;

    private static final long serialVersionUID = 1L;
}
