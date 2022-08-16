package com.fantechs.common.base.general.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "base_ht_process_category")
@Data
public class BaseHtProcessCategory {
    /**
     * 工序类别历史ID
     */
    @Id
    @Column(name = "ht_process_category_id")
    private Long htProcessCategoryId;

    /**
     * 工序类别ID
     */
    @Column(name = "process_category_id")
    private Long processCategoryId;

    /**
     * 工序类别代码
     */
    @Column(name = "process_category_code")
    private String processCategoryCode;

    /**
     * 工序类别名称
     */
    @Column(name = "process_category_name")
    private String processCategoryName;

    /**
     * 工序类别描述
     */
    @Column(name = "process_category_desc")
    private String processCategoryDesc;

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
     * 状态（0、无效 1、有效）
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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 获取工序类别历史ID
     *
     * @return ht_process_category_id - 工序类别历史ID
     */
    public Long getHtProcessCategoryId() {
        return htProcessCategoryId;
    }

    /**
     * 设置工序类别历史ID
     *
     * @param htProcessCategoryId 工序类别历史ID
     */
    public void setHtProcessCategoryId(Long htProcessCategoryId) {
        this.htProcessCategoryId = htProcessCategoryId;
    }

    /**
     * 获取工序类别ID
     *
     * @return process_category_id - 工序类别ID
     */
    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    /**
     * 设置工序类别ID
     *
     * @param processCategoryId 工序类别ID
     */
    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    /**
     * 获取工序类别代码
     *
     * @return process_category_code - 工序类别代码
     */
    public String getProcessCategoryCode() {
        return processCategoryCode;
    }

    /**
     * 设置工序类别代码
     *
     * @param processCategoryCode 工序类别代码
     */
    public void setProcessCategoryCode(String processCategoryCode) {
        this.processCategoryCode = processCategoryCode;
    }

    /**
     * 获取工序类别名称
     *
     * @return process_category_name - 工序类别名称
     */
    public String getProcessCategoryName() {
        return processCategoryName;
    }

    /**
     * 设置工序类别名称
     *
     * @param processCategoryName 工序类别名称
     */
    public void setProcessCategoryName(String processCategoryName) {
        this.processCategoryName = processCategoryName;
    }

    /**
     * 获取工序类别描述
     *
     * @return process_category_desc - 工序类别描述
     */
    public String getProcessCategoryDesc() {
        return processCategoryDesc;
    }

    /**
     * 设置工序类别描述
     *
     * @param processCategoryDesc 工序类别描述
     */
    public void setProcessCategoryDesc(String processCategoryDesc) {
        this.processCategoryDesc = processCategoryDesc;
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
