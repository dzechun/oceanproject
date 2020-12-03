package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "smt_supplier")
@Data
public class SmtSupplier extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 379038968866477984L;
    /**
     * 供应商ID
     */
    @Id
    @Column(name = "supplier_id")
    @ApiModelProperty("供应商ID")
    @NotNull(groups = update.class,message = "供应商id不能为空")
    private Long supplierId;

    /**
     * 供应商代码
     */
    @Column(name = "supplier_code")
    @ApiModelProperty("供应商代码")
    @Excel(name = "供应商代码", height = 20, width = 30)
    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name")
    @ApiModelProperty("供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30)
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;

    /**
     * 供应商描述
     */
    @Column(name = "supplier_desc")
    @ApiModelProperty("供应商描述")
    @Excel(name = "供应商描述", height = 20, width = 30)
    private String supplierDesc;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty("状态(0无效，1有效)")
    @Excel(name = "状态", height = 20, width = 30,replace = "(0_无效，1_有效)")
    private Byte status;

    /**
     * 身份标识（1、供应商 2、客户）
     */
    @Column(name = "supplier_type")
    @ApiModelProperty("身份标识（1、供应商 2、客户）")
    @Excel(name = "身份标识", height = 20, width = 30,replace = "(1_供应商，2_客户)")
    private Byte supplierType;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty("创建人Id")
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
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty("修改人id")
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
    @ApiModelProperty("修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty("逻辑删除（0、删除 1、正常）")
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
}
