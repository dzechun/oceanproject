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
 * 物料编码关联客户料号表
 * base_material_supplier
 * @author 18358
 * @date 2020-11-03 09:39:36
 */
@Data
@Table(name = "base_material_supplier")
public class BaseMaterialSupplier extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -4291625048924361712L;
    /**
     * 物料编码关联客户料号ID
     */
    @ApiModelProperty(name="materialSupplierId",value = "物料编码关联客户料号ID")
    @Id
    @Column(name = "material_supplier_id")
    @NotNull(groups = update.class,message = "物料编码关联客户料号ID不能为空")
    private Long materialSupplierId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    /**
     * 客户料号
     */
    @ApiModelProperty(name="materialSupplierCode",value = "客户料号")
    @Excel(name = "客户料号", height = 20, width = 30,orderNum="4")
    @Column(name = "material_supplier_code")
    @NotBlank(message = "客户料号不能为空")
    private String materialSupplierCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Column(name = "supplier_id")
    @NotNull(message = "客户ID不能为空")
    private Long supplierId;

    /**
     * 图片
     */
    @ApiModelProperty(name="image",value = "图片")
    @Column(name = "image")
    private String image;

    /**
     * 客户产品型号
     */
    @ApiModelProperty(name="supplierProductModel",value = "客户产品型号")
    @Column(name = "supplier_product_model")
    @Excel(name = "客户产品型号", height = 20, width = 30,orderNum="11")
    private String supplierProductModel;

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
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="7",replace = {"无效_0", "有效_1"})
    private Integer status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
