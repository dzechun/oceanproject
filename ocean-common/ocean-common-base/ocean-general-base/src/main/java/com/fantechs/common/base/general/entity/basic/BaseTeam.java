package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 班组信息表
 * base_team
 * @author 53203
 * @date 2021-01-15 15:14:37
 */
@Data
@Table(name = "base_team")
public class BaseTeam extends ValidGroup implements Serializable {
    /**
     * 班组ID
     */
    @ApiModelProperty(name="teamId",value = "班组ID")
    @Id
    @Column(name = "team_id")
    @NotNull(groups = update.class,message = "班组ID不能为空")
    private Long teamId;

    /**
     * 班组代码
     */
    @ApiModelProperty(name="teamCode",value = "班组代码")
    @Excel(name = "班组代码", height = 20, width = 30)
    @Column(name = "team_code")
    @NotBlank(message = "班组代码不能为空")
    private String teamCode;

    /**
     * 班组名称
     */
    @ApiModelProperty(name="teamName",value = "班组名称")
    @Excel(name = "班组名称", height = 20, width = 30)
    @Column(name = "team_name")
    private String teamName;

    /**
     * 班组描述
     */
    @ApiModelProperty(name="teamDesc",value = "班组描述")
    @Excel(name = "班组描述", height = 20, width = 30)
    @Column(name = "team_desc")
    private String teamDesc;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    @Column(name = "work_shop_id")
    private Long workShopId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}