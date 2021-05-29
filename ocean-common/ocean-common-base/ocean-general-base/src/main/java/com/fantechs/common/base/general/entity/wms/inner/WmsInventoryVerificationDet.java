package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 库存盘点明细
 * wms_inventory_verification_det
 * @author mr.lei
 * @date 2021-05-27 18:21:54
 */
@Data
@Table(name = "wms_inventory_verification_det")
public class WmsInventoryVerificationDet extends ValidGroup implements Serializable {
    @Id
    @Column(name = "inventory_verification_det_id")
    private Long inventoryVerificationDetId;

    @Column(name = "inventory_verification_id")
    private Long inventoryVerificationId;

    /**
     * 相关明细ID
     */
    @ApiModelProperty(name="sourceDetId",value = "相关明细ID")
    @Excel(name = "相关明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_det_id")
    private Long sourceDetId;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @Excel(name = "储位id", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Excel(name = "物料id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 原始数量
     */
    @ApiModelProperty(name="originalQty",value = "原始数量")
    @Excel(name = "原始数量", height = 20, width = 30,orderNum="") 
    @Column(name = "original_qty")
    private BigDecimal originalQty;

    /**
     * 盘点数量
     */
    @ApiModelProperty(name="inventoryQty",value = "盘点数量")
    @Excel(name = "盘点数量", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_qty")
    private BigDecimal inventoryQty;

    /**
     * 差异数量
     */
    @ApiModelProperty(name="discrepancyQty",value = "差异数量")
    @Excel(name = "差异数量", height = 20, width = 30,orderNum="") 
    @Column(name = "discrepancy_qty")
    private BigDecimal discrepancyQty;

    /**
     * 上次差异数量
     */
    @ApiModelProperty(name="upDiscrepancyQty",value = "上次差异数量")
    @Excel(name = "差异数量", height = 20, width = 30,orderNum="")
    @Column(name = "up_discrepancy_qty")
    private BigDecimal upDiscrepancyQty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 作业员
     */
    @ApiModelProperty(name="workName",value = "作业员")
    @Excel(name = "作业员", height = 20, width = 30,orderNum="") 
    @Column(name = "work_name")
    private String workName;

    /**
     * 是否登记 1-是 2-否
     */
    @ApiModelProperty(name="register",value = "是否登记 1-是 2-否")
    @Excel(name = "是否登记 1-是 2-否", height = 20, width = 30,orderNum="") 
    private Byte register;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

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

    private static final long serialVersionUID = 1L;
}