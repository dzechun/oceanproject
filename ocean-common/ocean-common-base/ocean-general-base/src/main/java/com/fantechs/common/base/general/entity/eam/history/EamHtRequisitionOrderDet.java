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
 * 设备领用单明细履历表
 * eam_ht_requisition_order_det
 * @author admin
 * @date 2021-06-29 10:35:10
 */
@Data
@Table(name = "eam_ht_requisition_order_det")
public class EamHtRequisitionOrderDet extends ValidGroup implements Serializable {
    /**
     * 设备领用单明细履历表ID
     */
    @ApiModelProperty(name="htRequisitionOrderDetId",value = "设备领用单明细履历表ID")
    @Excel(name = "设备领用单明细履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_requisition_order_det_id")
    private Long htRequisitionOrderDetId;

    /**
     * 设备领用单明细表ID
     */
    @ApiModelProperty(name="requisitionOrderDetId",value = "设备领用单明细表ID")
    @Excel(name = "设备领用单明细表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "requisition_order_det_id")
    private Long requisitionOrderDetId;

    /**
     * 设备领用单ID
     */
    @ApiModelProperty(name="requisitionOrderId",value = "设备领用单ID")
    @Excel(name = "设备领用单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "requisition_order_id")
    private Long requisitionOrderId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

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
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="1")
    @Transient
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="2")
    @Transient
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="3")
    @Transient
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="4")
    @Transient
    private String equipmentModel;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryDesc",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="5")
    private String equipmentCategoryDesc;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="8")
    private String warehouseName;

    /**
     * 领用时间
     */
    @ApiModelProperty(name="requisitionTime",value = "领用时间")
    @Excel(name = "领用时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Transient
    private Date requisitionTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}