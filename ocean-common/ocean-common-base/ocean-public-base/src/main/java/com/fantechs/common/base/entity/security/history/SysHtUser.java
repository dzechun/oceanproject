package com.fantechs.common.base.entity.security.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_ht_user")
@Data
public class SysHtUser {
    /**
     * 用户履历ID
     */
    @Id
    @Column(name = "ht_user_id")
    @ApiModelProperty(name="htUserId" ,value="用户履历ID")
    private Long htUserId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    @ApiModelProperty(name="userId" ,value="用户id")
    private Long userId;

    /**
     * 用户帐号
     */
    @Column(name = "user_name")
    @ApiModelProperty(name="userName" ,value="用户帐号")
    private String userName;

    /**
     * 用户工号
     */
    @Column(name = "user_code")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(name="nickName" ,value="用户名称")
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(name="password" ,value="密码")
    private String password;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="用户状态(0-无效 1-有效)")
    private Byte status;

    /**
     * 厂别id
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name="factoryId" ,value="厂别id")
    private Long factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @Excel(name = "厂别")
    private String factoryName;

    /**
     * 部门
     */
    @Column(name = "dept_id")
    @ApiModelProperty(name="deptId" ,value="部门id")
    private Long deptId;

    /**
     * 部门名称
     */
    @Transient
    @Excel(name = "部门")
    private String deptName;


    /**
     * 电话
     */
    @ApiModelProperty(name="telephone" ,value="电话")
    private String telephone;

    /**
     * 手机
     */
    @ApiModelProperty(name="mobile" ,value="手机")
    private String mobile;

    /**
     * 邮件地址
     */

    @ApiModelProperty(name="email" ,value="email")
    private String email;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建人
     */
    @ApiModelProperty(name="createUserName" ,value="创建人")
    @Transient
    private String createUserName;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改人
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人")
    @Transient
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
}