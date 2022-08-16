package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

/**
 * 组织用户关联表
 * sys_organization_user
 * @author 53203
 * @date 2021-01-13 16:41:55
 */
@Data
@Table(name = "sys_organization_user")
public class SysOrganizationUser extends ValidGroup implements Serializable {
    /**
     * 组织用户关系id
     */
    @ApiModelProperty(name="organizationUserId",value = "组织用户关系id")
    @Excel(name = "组织用户关系id", height = 20, width = 30)
    @Id
    @Column(name = "organization_user_id")
    private Long organizationUserId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 用户id
     */
    @ApiModelProperty(name="userId",value = "用户id")
    @Excel(name = "用户id", height = 20, width = 30)
    @Column(name = "user_id")
    private Long userId;

    private static final long serialVersionUID = 1L;
}
