package com.fantechs.common.base.entity.basic;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_supplier")
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

    /**
     * 获取供应商ID
     *
     * @return supplier_id - 供应商ID
     */
    public Long getSupplierId() {
        return supplierId;
    }

    /**
     * 设置供应商ID
     *
     * @param supplierId 供应商ID
     */
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * 获取供应商代码
     *
     * @return supplier_code - 供应商代码
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * 设置供应商代码
     *
     * @param supplierCode 供应商代码
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * 获取供应商名称
     *
     * @return supplier_name - 供应商名称
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * 设置供应商名称
     *
     * @param supplierName 供应商名称
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * 获取供应商描述
     *
     * @return supplier_desc - 供应商描述
     */
    public String getSupplierDesc() {
        return supplierDesc;
    }

    /**
     * 设置供应商描述
     *
     * @param supplierDesc 供应商描述
     */
    public void setSupplierDesc(String supplierDesc) {
        this.supplierDesc = supplierDesc;
    }

    /**
     * 获取状态（0、无效 1、有效）
     *
     * @return status - 状态（0、无效 1、有效）
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态（0、无效 1、有效）
     *
     * @param status 状态（0、无效 1、有效）
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取创建人ID
     *
     * @return create_user_id - 创建人ID
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人ID
     *
     * @param createUserId 创建人ID
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改人ID
     *
     * @return modified_user_id - 修改人ID
     */
    public Long getModifiedUserId() {
        return modifiedUserId;
    }

    /**
     * 设置修改人ID
     *
     * @param modifiedUserId 修改人ID
     */
    public void setModifiedUserId(Long modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    /**
     * 获取修改时间
     *
     * @return modified_time - 修改时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifiedTime 修改时间
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**
     * 获取逻辑删除（0、删除 1、正常）
     *
     * @return is_delete - 逻辑删除（0、删除 1、正常）
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * 设置逻辑删除（0、删除 1、正常）
     *
     * @param isDelete 逻辑删除（0、删除 1、正常）
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取扩展字段1
     *
     * @return option1 - 扩展字段1
     */
    public String getOption1() {
        return option1;
    }

    /**
     * 设置扩展字段1
     *
     * @param option1 扩展字段1
     */
    public void setOption1(String option1) {
        this.option1 = option1;
    }

    /**
     * 获取扩展字段2
     *
     * @return option2 - 扩展字段2
     */
    public String getOption2() {
        return option2;
    }

    /**
     * 设置扩展字段2
     *
     * @param option2 扩展字段2
     */
    public void setOption2(String option2) {
        this.option2 = option2;
    }

    /**
     * 获取扩展字段3
     *
     * @return option3 - 扩展字段3
     */
    public String getOption3() {
        return option3;
    }

    /**
     * 设置扩展字段3
     *
     * @param option3 扩展字段3
     */
    public void setOption3(String option3) {
        this.option3 = option3;
    }
}