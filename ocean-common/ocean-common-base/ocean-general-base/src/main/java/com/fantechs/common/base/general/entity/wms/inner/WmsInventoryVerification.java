package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

/**
 * 东鹏库存盘点
 * wms_inventory_verification
 * @author mr.lei
 * @date 2021-05-27 18:14:29
 */
@Data
@Table(name = "wms_inventory_verification")
public class WmsInventoryVerification extends ValidGroup implements Serializable {
    /**
     * 库存盘点ID
     */
    @ApiModelProperty(name="inventoryVerificationId",value = "库存盘点ID")
    @Excel(name = "库存盘点ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "inventory_verification_id")
    private Long inventoryVerificationId;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="inventoryVerificationCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_verification_code")
    private String inventoryVerificationCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="") 
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 盘点类型：1-货品 2-库位 3-全盘
     */
    @ApiModelProperty(name="inventoryVerificationType",value = "盘点类型：1-货品 2-库位 3-全盘")
    @Excel(name = "1-仓库 2-库位", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_verification_type")
    private Byte inventoryVerificationType;

    /**
     * 1-盘点 2-复盘
     */
    @ApiModelProperty(name="projectType",value = "1-盘点 2-复盘")
    @Excel(name = "1-盘点 2-复盘", height = 20, width = 30,orderNum="") 
    @Column(name = "project_type")
    private Byte projectType;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    @Excel(name = "仓库id", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 1-打开 2-待作业 3-作业中 4-作废 5完成
     */
    @ApiModelProperty(name="status",value = "1-打开 2-待作业 3-作业中 4-作废 5完成")
    @Excel(name = "1-打开 2-待作业 3-作业中 4-作废 5完成", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 盘点方式 1-PDA盘点 2-纸质盘点
     */
    @ApiModelProperty(name="inventoryVerificationMode",value = "盘点方式 1-PDA盘点 2-纸质盘点")
    @Excel(name = "盘点方式 1-PDA盘点 2-纸质盘点", height = 20, width = 30,orderNum="") 
    @Column(name = "`inventory_verification_ mode`")
    private Byte inventoryVerificationMode;

    /**
     * 盲盘 1-是 2-否
     */
    @ApiModelProperty(name="blind",value = "盲盘 1-是 2-否")
    @Excel(name = "盲盘 1-是 2-否", height = 20, width = 30,orderNum="") 
    private String blind;

    @Column(name = "max_storage_count")
    private Integer maxStorageCount;

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

    @Transient
    @ApiModelProperty(name = "inventoryVerificationDets",value = "盘点类型：货品/全盘明细")
    private List<WmsInventoryVerificationDet> inventoryVerificationDets;

    @Transient
    @ApiModelProperty(name = "storageList",value = "储位id")
    private List<Long> storageList;

    @Transient
    @ApiModelProperty(name = "type",value = "类型：(1-修改 2-盘点登记)")
    private byte type;

    private static final long serialVersionUID = 1L;
}