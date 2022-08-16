package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 员工信息表
 * base_staff
 * @author 53203
 * @date 2021-01-16 20:25:42
 */
@Data
@Table(name = "base_staff")
public class BaseStaff extends ValidGroup implements Serializable {
    /**
     * 员工id
     */
    @ApiModelProperty(name="staffId",value = "员工id")
    @Id
    @Column(name = "staff_id")
    @NotNull(groups = update.class,message = "员工ID不能为空")
    private Long staffId;


    /**
     * 员工编码
     */
    @ApiModelProperty(name="staffCode",value = "员工编码")
    @Excel(name = "员工编码", height = 20, width = 30)
    @Column(name = "staff_code")
    @NotBlank(message = "员工编码不能为空")
    private String staffCode;

    /**
     * 员工名称
     */
    @ApiModelProperty(name="staffName",value = "员工名称")
    @Excel(name = "员工名称", height = 20, width = 30)
    @Column(name = "staff_name")
    @NotBlank(message = "员工名称不能为空")
    private String staffName;

    /**
     * 员工描述
     */
    @ApiModelProperty(name="staffDesc",value = "员工描述")
    @Excel(name = "员工描述", height = 20, width = 30)
    @Column(name = "staff_desc")
    private String staffDesc;

    /**
     * 班组id
     */
    @ApiModelProperty(name="teamId",value = "班组id")
    @Column(name = "team_id")
    private Long teamId;

    /**
     * 身份证号
     */
    @ApiModelProperty(name="identityNumber",value = "身份证号")
    @Excel(name = "身份证号", height = 20, width = 30)
    @Column(name = "identity_number")
    private String identityNumber;

    /**
     * 是否团队计件（0、否 1、是）
     */
    @ApiModelProperty(name="isTeamPiecework",value = "是否团队计件（0、否 1、是）")
    @Excel(name = "是否团队计件（0、否 1、是）", height = 20, width = 30,replace = {"否_0", "是_1"})
    @Column(name = "is_team_piecework")
    private Byte isTeamPiecework;

    /**
     * 员工状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "员工状态（0、不启用 1、启用）")
    @Excel(name = "员工状态（0、不启用 1、启用）", height = 20, width = 30,replace = {"不启用_0", "启用_1"})
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
    @Column(name = "org_id")
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

    /**
     * 人员工种关系集合
     */
    @ApiModelProperty(name="isDelete",value = "人员工种关系集合")
    private List<BaseStaffProcess> baseStaffProcessList;

    private static final long serialVersionUID = 1L;
}