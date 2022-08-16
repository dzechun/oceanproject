package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

/**
 * 用户组织关系信息表
 * base_organization_user
 * @author 53203
 * @date 2020-12-29 19:59:52
 */
@Data
@Table(name = "base_organization_user")
public class BaseOrganizationUser extends ValidGroup implements Serializable {
    /**
     * 用户组织关系id
     */
    @ApiModelProperty(name="organizationUserId",value = "用户组织关系id")
    @Id
    @Column(name = "organization_user_id")
    @NotNull(groups = update.class,message = "用户组织关系id不能为空")
    private Long organizationUserId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 用户id
     */
    @ApiModelProperty(name="userId",value = "用户id")
    @Column(name = "user_id")
    private Long userId;

    private static final long serialVersionUID = 1L;
}
