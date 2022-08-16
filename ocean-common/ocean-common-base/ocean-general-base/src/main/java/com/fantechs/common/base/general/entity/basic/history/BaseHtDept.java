package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_ht_dept")
@Data
public class BaseHtDept implements Serializable {
    private static final long serialVersionUID = -6472312674013929076L;
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
     * 厂别名称
     */
    @Transient
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    private String factoryName;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;

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
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

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
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

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
