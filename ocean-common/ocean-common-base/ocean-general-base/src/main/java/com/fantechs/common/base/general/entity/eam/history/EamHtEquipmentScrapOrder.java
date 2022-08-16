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
 * 设备报废单履历表
 * eam_ht_equipment_scrap_order
 * @author admin
 * @date 2021-08-21 13:52:42
 */
@Data
@Table(name = "eam_ht_equipment_scrap_order")
public class EamHtEquipmentScrapOrder extends ValidGroup implements Serializable {
    /**
     * 设备报废单履历ID
     */
    @ApiModelProperty(name="htEquipmentScrapOrderId",value = "设备报废单履历ID")
    @Excel(name = "设备报废单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_scrap_order_id")
    private Long htEquipmentScrapOrderId;

    /**
     * 设备报废单ID
     */
    @ApiModelProperty(name="equipmentScrapOrderId",value = "设备报废单ID")
    @Excel(name = "设备报废单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_scrap_order_id")
    private Long equipmentScrapOrderId;

    /**
     * 设备报废单编码
     */
    @ApiModelProperty(name="equipmentScrapOrderCode",value = "设备报废单编码")
    @Excel(name = "设备报废单编码", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_scrap_order_code")
    private String equipmentScrapOrderCode;

    /**
     * 申请用户ID
     */
    @ApiModelProperty(name="applyUserId",value = "申请用户ID")
    @Excel(name = "申请用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "apply_user_id")
    private Long applyUserId;

    /**
     * 申请部门ID
     */
    @ApiModelProperty(name="applyDeptId",value = "申请部门ID")
    @Excel(name = "申请部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "apply_dept_id")
    private Long applyDeptId;

    /**
     * 申请时间
     */
    @ApiModelProperty(name="applyTime",value = "申请时间")
    @Excel(name = "申请时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * 使用人ID
     */
    @ApiModelProperty(name="useUserId",value = "使用人ID")
    @Excel(name = "使用人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "use_user_id")
    private Long useUserId;

    /**
     * 单据状态(1-待确认 2-已确认)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待确认 2-已确认)")
    @Excel(name = "单据状态(1-待确认 2-已确认)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 使用部门ID
     */
    @ApiModelProperty(name="useDeptId",value = "使用部门ID")
    @Excel(name = "使用部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "use_dept_id")
    private Long useDeptId;

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
     * 申请部门
     */
    @Transient
    @ApiModelProperty(name = "applyDeptName",value = "申请部门")
    @Excel(name = "申请部门", height = 20, width = 30,orderNum="3")
    private String applyDeptName;

    /**
     * 使用人成本中心
     */
    @Transient
    @ApiModelProperty(name = "useDeptName",value = "使用人成本中心")
    @Excel(name = "使用人成本中心", height = 20, width = 30,orderNum="6")
    private String useDeptName;

    /**
     * 申请人姓名
     */
    @Transient
    @ApiModelProperty(name = "applyUserName",value = "申请人姓名")
    @Excel(name = "申请人姓名", height = 20, width = 30,orderNum="2")
    private String applyUserName;

    /**
     * 使用人姓名
     */
    @Transient
    @ApiModelProperty(name = "useUserName",value = "使用人姓名")
    @Excel(name = "使用人姓名", height = 20, width = 30,orderNum="5")
    private String useUserName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}