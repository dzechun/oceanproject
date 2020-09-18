package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
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
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long deptId;

    /**
     * 部门代码
     */
    @Column(name = "dept_code")
    @ApiModelProperty(name="deptCode" ,value="部门代码")
    @Excel(name = "部门代码", height = 20, width = 30)
    private String deptCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    @ApiModelProperty(name="deptName" ,value="部门名称")
    @Excel(name = "部门名称", height = 20, width = 30)
    private String deptName;

    /**
     * 部门描述
     */
    @Column(name = "dept_desc")
    @ApiModelProperty(name="deptDesc" ,value="部门描述")
    @Excel(name = "部门代码", height = 20, width = 30)
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
    @Excel(name = "厂别", height = 20, width = 30)
    private String factoryName;

    /**
     * 状态
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;


    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
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