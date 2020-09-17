package com.fantechs.common.base.entity.sysmanage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_dept")
@Data
public class SmtDept implements Serializable {

    private static final long serialVersionUID = -9208178506642851416L;
    /**
     * 部门ID
     */
    @Id
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 部门代码
     */
    @Column(name = "dept_code")
    @Excel(name = "部门代码", height = 20, width = 30)
    private String deptCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    @Excel(name = "部门名称", height = 20, width = 30)
    private String deptName;

    /**
     * 部门描述
     */
    @Column(name = "dept_desc")
    @Excel(name = "部门代码", height = 20, width = 30)
    private String deptDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @Excel(name = "厂别名称", height = 20, width = 30)
    private String factoryName;

    /**
     * 状态
     */
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @Excel(name = "创建账号名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    private Long modifiedUserId;
    /**
     * 修改账号名称
     */
    @Transient
    @Excel(name = "修改账号名称", height = 20, width = 30)
    private String modifiedUserName;


    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
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