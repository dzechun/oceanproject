package com.fantechs.common.base.entity.sysmanage;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_user_role")
@Data
public class SmtUserRole implements Serializable {

    private static final long serialVersionUID = -7109497559493175283L;
    /**
     * id
     */
    @Id
    @Column(name = "user_role_id")
    private String userRoleId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

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