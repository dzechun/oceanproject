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
 * 治具保养单明细履历表
 * eam_ht_jig_maintain_order_det
 * @author admin
 * @date 2021-08-13 09:16:45
 */
@Data
@Table(name = "eam_ht_jig_maintain_order_det")
public class EamHtJigMaintainOrderDet extends ValidGroup implements Serializable {
    /**
     * 治具保养单明细履历ID
     */
    @ApiModelProperty(name="htJigMaintainOrderDetId",value = "治具保养单明细履历ID")
    @Excel(name = "治具保养单明细履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_maintain_order_det_id")
    private Long htJigMaintainOrderDetId;

    /**
     * 治具保养单明细ID
     */
    @ApiModelProperty(name="jigMaintainOrderDetId",value = "治具保养单明细ID")
    @Excel(name = "治具保养单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_order_det_id")
    private Long jigMaintainOrderDetId;

    /**
     * 治具保养单ID
     */
    @ApiModelProperty(name="jigMaintainOrderId",value = "治具保养单ID")
    @Excel(name = "治具保养单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_order_id")
    private Long jigMaintainOrderId;

    /**
     * 治具保养事项ID
     */
    @ApiModelProperty(name="jigMaintainProjectItemId",value = "治具保养事项ID")
    @Excel(name = "治具保养事项ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_maintain_project_item_id")
    private Long jigMaintainProjectItemId;

    /**
     * 定量结果
     */
    @ApiModelProperty(name="rationResult",value = "定量结果")
    @Excel(name = "定量结果", height = 20, width = 30,orderNum="") 
    @Column(name = "ration_result")
    private BigDecimal rationResult;

    /**
     * 定性结果(0-否 1-是)
     */
    @ApiModelProperty(name="qualitative",value = "定性结果(0-否 1-是)")
    @Excel(name = "定性结果(0-否 1-是)", height = 20, width = 30,orderNum="") 
    private Byte qualitative;

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

    /**
     * 保养事项名称
     */
    @Transient
    @ApiModelProperty(name = "jigMaintainProjectName",value = "保养事项名称")
    @Excel(name = "保养事项名称", height = 20, width = 30,orderNum="6")
    private String jigMaintainProjectName;

    /**
     * 保养事项描述
     */
    @Transient
    @ApiModelProperty(name = "jigMaintainProjectDesc",value = "保养事项描述")
    @Excel(name = "保养事项描述", height = 20, width = 30,orderNum="6")
    private String jigMaintainProjectDesc;

    /**
     * 判定类别(1-定性 2-定量)
     */
    @Transient
    @ApiModelProperty(name = "judgeType",value = "判定类别(1-定性 2-定量)")
    @Excel(name = "判定类别(1-定性 2-定量)", height = 20, width = 30,orderNum="6")
    private Byte judgeType;

    /**
     * 定量下限
     */
    @Transient
    @ApiModelProperty(name = "rationFloor",value = "定量下限")
    @Excel(name = "定量下限", height = 20, width = 30,orderNum="6")
    private BigDecimal rationFloor;

    /**
     * 定量上限
     */
    @Transient
    @ApiModelProperty(name = "rationUpperLimit",value = "定量上限")
    @Excel(name = "定量上限", height = 20, width = 30,orderNum="6")
    private BigDecimal rationUpperLimit;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}