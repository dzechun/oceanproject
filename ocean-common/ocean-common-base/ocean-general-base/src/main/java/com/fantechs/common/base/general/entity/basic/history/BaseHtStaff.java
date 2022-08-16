package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 员工信息履历表
 * base_ht_staff
 * @author 53203
 * @date 2021-01-16 20:25:42
 */
@Data
@Table(name = "base_ht_staff")
public class BaseHtStaff extends ValidGroup implements Serializable {
    /**
     * 员工履历id
     */
    @ApiModelProperty(name="htStaffId",value = "员工履历id")
    @Excel(name = "员工履历id", height = 20, width = 30)
    @Id
    @Column(name = "ht_staff_id")
    private Long htStaffId;

    /**
     * 员工id
     */
    @ApiModelProperty(name="staffId",value = "员工id")
    @Excel(name = "员工id", height = 20, width = 30)
    @Column(name = "staff_id")
    private Long staffId;

    /**
     * 班组id
     */
    @ApiModelProperty(name="teamId",value = "班组id")
    @Excel(name = "班组id", height = 20, width = 30)
    @Column(name = "team_id")
    private Long teamId;

    /**
     * 员工编码
     */
    @ApiModelProperty(name="staffCode",value = "员工编码")
    @Excel(name = "员工编码", height = 20, width = 30)
    @Column(name = "staff_code")
    private String staffCode;

    /**
     * 员工名称
     */
    @ApiModelProperty(name="staffName",value = "员工名称")
    @Excel(name = "员工名称", height = 20, width = 30)
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 员工描述
     */
    @ApiModelProperty(name="staffDesc",value = "员工描述")
    @Excel(name = "员工描述", height = 20, width = 30)
    @Column(name = "staff_desc")
    private String staffDesc;

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
    @Excel(name = "是否团队计件（0、否 1、是）", height = 20, width = 30)
    @Column(name = "is_team_piecework")
    private Byte isTeamPiecework;

    /**
     * 员工状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "员工状态（0、不启用 1、启用）")
    @Excel(name = "员工状态（0、不启用 1、启用）", height = 20, width = 30)
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
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
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
    @Excel(name = "修改人ID", height = 20, width = 30)
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
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 班组代码
     */
    @Transient
    @ApiModelProperty(name="teamCode",value = "班组代码")
    private String teamCode;

    /**
     * 班组名称
     */
    @Transient
    @ApiModelProperty(name="teamName",value = "班组名称")
    private String teamName;

    /**
     * 班组描述
     */
    @Transient
    @ApiModelProperty(name="teamDesc",value = "班组描述")
    private String teamDesc;

    /**
     * 车间编码
     */
    @Transient
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    @Excel(name = "车间名称", height = 20, width = 30)
    private String workShopName;

    /**
     * 车间描述
     */
    @Transient
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    private String workShopDesc;

    /**
     * 工厂名称
     */
    @Transient
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    @Excel(name = "工厂名称", height = 20, width = 30)
    private String factoryName;

    /**
     * 工厂编码
     */
    @Transient
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    private String factoryCode;

    /**
     * 工厂描述
     */
    @Transient
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;
    private static final long serialVersionUID = 1L;
}