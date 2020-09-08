package com.fantechs.common.base.entity.sysmanage.history;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "smt_ht_dept")
@Data
public class SmtHtDept {
    /**
     * 部门履历ID
     */
    @Id
    @Column(name = "ht_dept_id")
    private String htDeptId;

    /**
     * 部门代码
     */
    @Column(name = "dept_code")
    private String deptCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 部门描述
     */
    @Column(name = "dept_desc")
    private String deptDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    private String factoryId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    private String modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    private Date modifiedTime;

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