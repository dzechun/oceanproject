package com.fantechs.common.base.general.entity.mes.sfc;

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
 * 车间管理-维修单半成品
 * mes_sfc_repair_order_semi_product
 * @author admin
 * @date 2021-09-10 11:18:50
 */
@Data
@Table(name = "mes_sfc_repair_order_semi_product")
public class MesSfcRepairOrderSemiProduct extends ValidGroup implements Serializable {
    /**
     * 维修单半成品ID
     */
    @ApiModelProperty(name="repairOrderSemiProductId",value = "维修单半成品ID")
    @Excel(name = "维修单半成品ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "repair_order_semi_product_id")
    private Long repairOrderSemiProductId;

    /**
     * 维修单ID
     */
    @ApiModelProperty(name="repairOrderId",value = "维修单ID")
    @Excel(name = "维修单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "repair_order_id")
    private Long repairOrderId;

    /**
     * 半成品条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "半成品条码ID")
    @Excel(name = "半成品条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 半成品条码
     */
    @ApiModelProperty(name="barcode",value = "半成品条码")
    @Excel(name = "半成品条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 半成品物料ID
     */
    @ApiModelProperty(name="materialId",value = "半成品物料ID")
    @Excel(name = "半成品物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="11")
    private String materialCode;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="11")
    private String materialDesc;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}