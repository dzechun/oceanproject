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
import java.util.Date;

;
;

/**
 * 设备维修单替换件履历表
 * eam_ht_equ_repair_order_replacement
 * @author admin
 * @date 2021-08-20 15:53:41
 */
@Data
@Table(name = "eam_ht_equ_repair_order_replacement")
public class EamHtEquRepairOrderReplacement extends ValidGroup implements Serializable {
    /**
     * 设备维修单替换件履历ID
     */
    @ApiModelProperty(name="htEquRepairOrderReplacementId",value = "设备维修单替换件履历ID")
    @Excel(name = "设备维修单替换件履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equ_repair_order_replacement_id")
    private Long htEquRepairOrderReplacementId;

    /**
     * 设备维修单替换件ID
     */
    @ApiModelProperty(name="equRepairOrderReplacementId",value = "设备维修单替换件ID")
    @Excel(name = "设备维修单替换件ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equ_repair_order_replacement_id")
    private Long equRepairOrderReplacementId;

    /**
     * 设备维修单ID
     */
    @ApiModelProperty(name="equipmentRepairOrderId",value = "设备维修单ID")
    @Excel(name = "设备维修单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_repair_order_id")
    private Long equipmentRepairOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="usageQty",value = "使用数量")
    @Excel(name = "使用数量", height = 20, width = 30,orderNum="") 
    @Column(name = "usage_qty")
    private Integer usageQty;

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
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}