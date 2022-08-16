package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 物料编码关联客户料号履历表
 * base_ht_material_supplier
 * @author admin
 * @date 2021-10-26 09:57:04
 */
@Data
@Table(name = "base_ht_material_supplier")
public class BaseHtMaterialSupplier extends ValidGroup implements Serializable {
    /**
     * 物料编码关联客户料号履历ID
     */
    @ApiModelProperty(name="htMaterialSupplierId",value = "物料编码关联客户料号履历ID")
    @Excel(name = "物料编码关联客户料号履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_material_supplier_id")
    private Long htMaterialSupplierId;

    /**
     * 物料编码关联客户料号ID
     */
    @ApiModelProperty(name="materialSupplierId",value = "物料编码关联客户料号ID")
    @Excel(name = "物料编码关联客户料号ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_supplier_id")
    private Long materialSupplierId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    @Column(name = "material_supplier_code")
    private String materialSupplierCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    private String image;

    /**
     * 客户产品型号
     */
    @ApiModelProperty(name="supplierProductModel",value = "客户产品型号")
    @Excel(name = "客户产品型号", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_product_model")
    private String supplierProductModel;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum = "1")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @Transient
    @Excel(name = "物料版本", height = 20, width = 30,orderNum = "2")
    private String materialVersion;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum = "3")
    private String materialDesc;

    /**
     * 客户名称
     */
    @Column(name = "supplier_name")
    @ApiModelProperty(name = "supplierName",value = "客户名称")
    @Excel(name = "客户名称", height = 20, width = 30,orderNum = "5")
    private String supplierName;

    /**
     * 客户编码
     */
    @Column(name = "supplier_code")
    @ApiModelProperty(name = "supplierCode",value = "客户编码")
    @Excel(name = "客户编码", height = 20, width = 30,orderNum = "6")
    private String supplierCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}