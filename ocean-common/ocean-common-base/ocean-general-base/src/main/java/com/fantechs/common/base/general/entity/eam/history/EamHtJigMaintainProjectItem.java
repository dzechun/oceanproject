package com.fantechs.common.base.general.entity.eam.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 治具保养事项履历表
 * eam_ht_jig_maintain_project_item
 * @author admin
 * @date 2021-08-12 15:09:26
 */
@Data
@Table(name = "eam_ht_jig_maintain_project_item")
public class EamHtJigMaintainProjectItem extends ValidGroup implements Serializable {
    /**
     * 治具保养事项履历ID
     */
    @ApiModelProperty(name="htJigMaintainProjectItemId",value = "治具保养事项履历ID")
    @Excel(name = "治具保养事项履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_maintain_project_item_id")
    private Long htJigMaintainProjectItemId;

    /**
     * 治具保养事项ID
     */
    @ApiModelProperty(name="jigMaintainProjectItemId",value = "治具保养事项ID")
    @Excel(name = "治具保养事项ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_project_item_id")
    private Long jigMaintainProjectItemId;

    /**
     * 治具保养项目ID
     */
    @ApiModelProperty(name="jigMaintainProjectId",value = "治具保养项目ID")
    @Excel(name = "治具保养项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_project_id")
    private Long jigMaintainProjectId;

    /**
     * 治具保养事项名称
     */
    @ApiModelProperty(name="jigMaintainProjectName",value = "治具保养事项名称")
    @Excel(name = "治具保养事项名称", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_project_name")
    private String jigMaintainProjectName;

    /**
     * 治具保养事项描述
     */
    @ApiModelProperty(name="jigMaintainProjectDesc",value = "治具保养事项描述")
    @Excel(name = "治具保养事项描述", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_project_desc")
    private String jigMaintainProjectDesc;

    /**
     * 判定类别(1-定性 2-定量)
     */
    @ApiModelProperty(name="judgeType",value = "判定类别(1-定性 2-定量)")
    @Excel(name = "判定类别(1-定性 2-定量)", height = 20, width = 30,orderNum="") 
    @Column(name = "judge_type")
    private Byte judgeType;

    /**
     * 定量下限
     */
    @ApiModelProperty(name="rationFloor",value = "定量下限")
    @Excel(name = "定量下限", height = 20, width = 30,orderNum="")
    @Column(name = "ration_floor")
    private BigDecimal rationFloor;

    /**
     * 定量上限
     */
    @ApiModelProperty(name="rationUpperLimit",value = "定量上限")
    @Excel(name = "定量上限", height = 20, width = 30,orderNum="")
    @Column(name = "ration_upper_limit")
    private BigDecimal rationUpperLimit;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}