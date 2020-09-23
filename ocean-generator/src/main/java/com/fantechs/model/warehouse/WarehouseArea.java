package com.fantechs.model.warehouse;

import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_warehouse_area")
public class WarehouseArea {
    /**
     * 仓库区域ID
     */
    @Id
    @Column(name = "warehouse_area_id")
    private Long warehouseAreaId;

    /**
     * 仓库区域编码
     */
    @Column(name = "warehouse_area_code")
    private String warehouseAreaCode;

    /**
     * 仓库区域名称
     */
    @Column(name = "warehouse_area_name")
    private String warehouseAreaName;

    /**
     * 仓库区域描述
     */
    @Column(name = "warehouse_area_desc")
    private String warehouseAreaDesc;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 状态(0无效，1有效)
     */
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
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
     * 获取仓库区域ID
     *
     * @return warehouse_area_id - 仓库区域ID
     */
    public Long getWarehouseAreaId() {
        return warehouseAreaId;
    }

    /**
     * 设置仓库区域ID
     *
     * @param warehouseAreaId 仓库区域ID
     */
    public void setWarehouseAreaId(Long warehouseAreaId) {
        this.warehouseAreaId = warehouseAreaId;
    }

    /**
     * 获取仓库区域编码
     *
     * @return warehouse_area_code - 仓库区域编码
     */
    public String getWarehouseAreaCode() {
        return warehouseAreaCode;
    }

    /**
     * 设置仓库区域编码
     *
     * @param warehouseAreaCode 仓库区域编码
     */
    public void setWarehouseAreaCode(String warehouseAreaCode) {
        this.warehouseAreaCode = warehouseAreaCode;
    }

    /**
     * 获取仓库区域名称
     *
     * @return warehouse_area_name - 仓库区域名称
     */
    public String getWarehouseAreaName() {
        return warehouseAreaName;
    }

    /**
     * 设置仓库区域名称
     *
     * @param warehouseAreaName 仓库区域名称
     */
    public void setWarehouseAreaName(String warehouseAreaName) {
        this.warehouseAreaName = warehouseAreaName;
    }

    /**
     * 获取仓库区域描述
     *
     * @return warehouse_area_desc - 仓库区域描述
     */
    public String getWarehouseAreaDesc() {
        return warehouseAreaDesc;
    }

    /**
     * 设置仓库区域描述
     *
     * @param warehouseAreaDesc 仓库区域描述
     */
    public void setWarehouseAreaDesc(String warehouseAreaDesc) {
        this.warehouseAreaDesc = warehouseAreaDesc;
    }

    /**
     * 获取仓库ID
     *
     * @return warehouse_id - 仓库ID
     */
    public Long getWarehouseId() {
        return warehouseId;
    }

    /**
     * 设置仓库ID
     *
     * @param warehouseId 仓库ID
     */
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * 获取状态(0无效，1有效)
     *
     * @return status - 状态(0无效，1有效)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态(0无效，1有效)
     *
     * @param status 状态(0无效，1有效)
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