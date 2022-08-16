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
 * 治具领用履历表
 * eam_ht_jig_requisition
 * @author admin
 * @date 2021-07-30 09:22:00
 */
@Data
@Table(name = "eam_ht_jig_requisition")
public class EamHtJigRequisition extends ValidGroup implements Serializable {
    /**
     * 治具领用履历表ID
     */
    @ApiModelProperty(name="htJigRequisitionId",value = "治具领用履历表ID")
    @Excel(name = "治具领用履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_requisition_id")
    private Long htJigRequisitionId;

    /**
     * 治具领用ID
     */
    @ApiModelProperty(name="jigRequisitionId",value = "治具领用ID")
    @Excel(name = "治具领用ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_requisition_id")
    private Long jigRequisitionId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="")
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Excel(name = "治具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Excel(name = "治具条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 本次使用次数
     */
    @ApiModelProperty(name="thisTimeUsageTime",value = "本次使用次数")
    @Excel(name = "本次使用次数", height = 20, width = 30,orderNum="") 
    @Column(name = "this_time_usage_time")
    private Integer thisTimeUsageTime;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="15")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="17")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="2")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="4")
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="5")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="6")
    private String jigModel;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="7")
    private String jigCategoryName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="1")
    private String jigBarcode;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="8")
    private String warehouseName;

    /**
     * 库区
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区")
    @Excel(name = "库区", height = 20, width = 30,orderNum="9")
    private String warehouseAreaName;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="10")
    private String storageCode;

    /**
     * 领用状态（0-已归还 1-已领用）
     */
    @Transient
    @ApiModelProperty(name = "requisitionStatus",value = "领用状态（0-已归还 1-已领用）")
    @Excel(name = "领用状态（0-已归还 1-已领用）", height = 20, width = 30,orderNum="11")
    private Byte requisitionStatus;

    /**
     * 领用时间
     */
    @Transient
    @ApiModelProperty(name = "requisitionTime",value = "领用时间")
    @Excel(name = "领用时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date requisitionTime;

    /**
     * 归还时间
     */
    @Transient
    @ApiModelProperty(name = "returnTime",value = "归还时间")
    @Excel(name = "归还时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}