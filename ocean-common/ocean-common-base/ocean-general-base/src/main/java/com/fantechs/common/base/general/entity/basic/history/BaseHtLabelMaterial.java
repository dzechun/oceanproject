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
 * 产品物料关联标签历史信息
 * bcm_ht_label_material
 * @author mr.lei
 * @date 2020-12-17 19:34:46
 */
@Data
@Table(name = "base_ht_label_material")
public class BaseHtLabelMaterial extends ValidGroup implements Serializable {
    /**
     * 产品关联标签历史id
     */
    @ApiModelProperty(name="labelHtMaterialId",value = "产品关联标签历史id")
    @Excel(name = "产品关联标签历史id", height = 20, width = 30)
    @Id
    @Column(name = "label_ht_material_id")
    private Long labelHtMaterialId;

    /**
     * 产品关联标签id
     */
    @ApiModelProperty(name="labelMaterialId",value = "产品关联标签id")
    @Excel(name = "产品关联标签id", height = 20, width = 30)
    @Column(name = "label_material_id")
    private Long labelMaterialId;

    /**
     * 产品物料id
     */
    @ApiModelProperty(name="materialId",value = "产品物料id")
    @Excel(name = "产品物料id", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 标签信息id
     */
    @ApiModelProperty(name="labelId",value = "标签信息id")
    @Excel(name = "标签信息id", height = 20, width = 30)
    @Column(name = "label_id")
    private Long labelId;

    /**
     * 工序信息id
     */
    @ApiModelProperty(name="processId",value = "工序信息id")
    @Excel(name = "工序信息id", height = 20, width = 30)
    @Column(name = "process_id")
    private Long processId;

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
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码",height = 20,width = 30,orderNum = "6")
    private String materialCode;
    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    @Excel(name = "物料版本",height = 20,width = 30,orderNum = "6")
    private String materialVersion;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    @Excel(name = "物料描述",height = 20,width = 30,orderNum = "6")
    private String materialDesc;
    /**
     * 标签编码
     */
    @Transient
    @ApiModelProperty(name = "labelCode",value = "标签编码")
    @Excel(name = "标签编码",height = 20,width = 30,orderNum = "6")
    private String labelCode;
    /**
     * 标签名称
     */
    @Transient
    @ApiModelProperty(name = "labelName",value = "标签名称")
    @Excel(name = "标签名称",height = 20,width = 30,orderNum = "6")
    private String labelName;
    /**
     * 标签版本
     */
    @Transient
    @ApiModelProperty(name = "labelVersion",value = "标签版本")
    @Excel(name = "标签版本",height = 20,width = 30,orderNum = "6")
    private String labelVersion;
    /**
     * 标签类别
     */
    @Transient
    @ApiModelProperty(name = "labelCategoryName",value = "标签类别")
    @Excel(name = "标签类别",height = 20,width = 30,orderNum = "6")
    private String labelCategoryName;
    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    @Excel(name = "工序名称",height = 20,width = 30,orderNum = "6")
    private String processName;
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
     * 是否工序打印（Y/N）
     */
    @ApiModelProperty(name="isProcess",value = "是否工序打印（Y/N）")
    @Column(name = "is_process")
    private Byte isProcess;

    private static final long serialVersionUID = 1L;
}