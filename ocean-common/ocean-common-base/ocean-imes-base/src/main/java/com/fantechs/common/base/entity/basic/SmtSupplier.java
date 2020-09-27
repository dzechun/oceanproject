package com.fantechs.common.base.entity.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_supplier")
@Data
public class SmtSupplier implements Serializable {
    private static final long serialVersionUID = 379038968866477984L;
    /**
     * 供应商ID
     */
    @Id
    @Column(name = "supplier_id")
    @ApiModelProperty("供应商ID")
    private Long supplierId;

    /**
     * 供应商代码
     */
    @Column(name = "supplier_code")
    @ApiModelProperty("供应商代码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name")
    @ApiModelProperty("供应商名称")
    private String supplierName;

    /**
     * 供应商描述
     */
    @Column(name = "supplier_desc")
    @ApiModelProperty("供应商描述")
    private String supplierDesc;

    /**
     * 状态（0、无效 1、有效）
     */
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty("创建人Id")
    private Long createUserId;

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