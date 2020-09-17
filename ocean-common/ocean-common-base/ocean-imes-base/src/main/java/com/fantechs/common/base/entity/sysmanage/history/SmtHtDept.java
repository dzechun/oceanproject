package com.fantechs.common.base.entity.sysmanage.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_dept")
@Data
public class SmtHtDept implements Serializable {
    /**
     * 部门履历ID
     */
    @Id
    @Column(name = "ht_dept_id")
    private Long htDeptId;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    @ApiModelProperty(name="deptId" ,value="部门ID")
    private Long deptId;
    /**
     * 部门代码
     */
    @Column(name = "dept_code")
    @ApiModelProperty(name="deptCode" ,value="部门代码")
    private String deptCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    @ApiModelProperty(name="deptName" ,value="部门名称")
    private String deptName;

    /**
     * 部门描述
     */
    @Column(name = "dept_desc")
    @ApiModelProperty(name="deptDesc" ,value="部门描述")
    private String deptDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name="factoryId" ,value="厂别ID")
    private Long factoryId;

    /**
     * 状态
     */
    @ApiModelProperty(name="status" ,value="状态")
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
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