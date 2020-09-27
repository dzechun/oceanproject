package com.fantechs.model.workshop;

import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_workshop_section")
public class WorkshopSection {
    /**
     * 工段ID
     */
    @Id
    @Column(name = "section_id")
    private Long sectionId;

    /**
     * 工段代码
     */
    @Column(name = "section_code")
    private String sectionCode;

    /**
     * 工段名称
     */
    @Column(name = "section_name")
    private String sectionName;

    /**
     * 工段描述
     */
    @Column(name = "section_desc")
    private String sectionDesc;

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
     * 获取工段ID
     *
     * @return section_id - 工段ID
     */
    public Long getSectionId() {
        return sectionId;
    }

    /**
     * 设置工段ID
     *
     * @param sectionId 工段ID
     */
    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * 获取工段代码
     *
     * @return section_code - 工段代码
     */
    public String getSectionCode() {
        return sectionCode;
    }

    /**
     * 设置工段代码
     *
     * @param sectionCode 工段代码
     */
    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    /**
     * 获取工段名称
     *
     * @return section_name - 工段名称
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * 设置工段名称
     *
     * @param sectionName 工段名称
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * 获取工段描述
     *
     * @return section_desc - 工段描述
     */
    public String getSectionDesc() {
        return sectionDesc;
    }

    /**
     * 设置工段描述
     *
     * @param sectionDesc 工段描述
     */
    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
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