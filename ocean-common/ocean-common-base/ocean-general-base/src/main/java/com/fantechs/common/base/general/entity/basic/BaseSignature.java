package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "base_signature")
@Data
public class BaseSignature extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 2443310438091476124L;
    /**
     * 特征码ID
     */
    @Id
    @Column(name = "signature_id")
    @ApiModelProperty(name="signatureId" ,value="特征码ID")
    @NotNull(groups = update.class,message = "特征码id不能为空")
    private Long signatureId;

    /**
     * 物料ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="物料ID")
    @NotNull(message = "物料id不能为空")
    private Long materialId;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 特征码
     */
    @Column(name = "signature_code")
    @ApiModelProperty(name="signatureCode" ,value="特征码")
    @Excel(name = "特征码", height = 20, width = 30)
    @NotBlank(message = "特征码编码不能为空")
    private String signatureCode;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id")
    @ApiModelProperty(name="supplierId" ,value="供应商ID")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name="supplierName" ,value="供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30)
    private String supplierName;

    /**
     * 供应商编码
     */
    @Transient
    @ApiModelProperty(name="supplierCode" ,value="供应商编码")
    @Excel(name = "供应商编码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 特征码正则
     */
    @Column(name = "signature_regex")
    @ApiModelProperty(name="signatureRegex" ,value="特征码正则")
    @Excel(name = "特征码正则", height = 20, width = 30)
    private String signatureRegex;

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
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

    @Column(name = "fixed_signature")
    @ApiModelProperty(name="fixedSignature" ,value="固定特征码")
    private String fixedSignature;

    @Column(name = "model_signature")
    @ApiModelProperty(name="modelSignature" ,value="型号特征码")
    private String modelSignature;

}
