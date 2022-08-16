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
@Table(name = "wms_inner_stock_order")
public class WmsInnerStockOrder extends ValidGroup implements Serializable {
    /**
     * 库存盘点ID
     */
    @ApiModelProperty(name="stockOrderId",value = "库存盘点ID")
    @Id
    @Column(name = "stock_order_id")
    private Long stockOrderId;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stockOrderCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="1")
    @Column(name = "stock_order_code")
    private String stockOrderCode;

    /**
     * 货主
     */
    @ApiModelProperty(name = "materialOwnerId",value = "货主")
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="3")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 盘点类型：1-库位 2-货品 3-全盘
     */
    @ApiModelProperty(name="stockType",value = "盘点类型：1-库位 2-货品 3-全盘")
    @Excel(name = "盘点类型", height = 20, width = 30,orderNum="4",replace = {"库位_1","货品_2","全盘_3"})
    @Column(name = "stock_type")
    private Byte stockType;

    /**
     * 1-盘点 2-复盘
     */
    @ApiModelProperty(name="projectType",value = "1-盘点 2-复盘")
    @Excel(name = "作业类型", height = 20, width = 30,orderNum="5",replace = {"盘点_1","复盘_2"})
    @Column(name = "project_type")
    private Byte projectType;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 1-打开 2-待作业 3-作业中 4-作废 5完成
     */
    @ApiModelProperty(name="orderStatus",value = "1-打开 2-待作业 3-作业中 4-作废 5完成")
    @Excel(name = "状态", height = 20, width = 30,orderNum="7",replace = {"打开_1"," 待作业_2"," 作业中_3"," 作废_4","待处理_5","完成_6"})
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 盘点方式 1-PDA盘点 2-纸质盘点
     */
    @ApiModelProperty(name="stockMode",value = "盘点方式 1-PDA盘点 2-纸质盘点")
    @Excel(name = "盘点方式", height = 20, width = 30,orderNum="8",replace = {"PDA盘点_1","纸质盘点_2"})
    @Column(name = "`stock_mode`")
    private Byte stockMode;

    /**
     * 盲盘 1-是 2-否
     */
    @ApiModelProperty(name="blind",value = "盲盘 1-是 2-否")
    @Excel(name = "盲盘", height = 20, width = 30,orderNum="9",replace = {"是_1","否_2"})
    private String ifBlindStock;

    @Column(name = "max_storage_count")
    private Integer maxStorageCount;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="10")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    @Transient
    @ApiModelProperty(name = "inventoryVerificationDets",value = "盘点类型：货品/全盘明细")
    private List<WmsInnerStockOrderDet> inventoryVerificationDets;

    @Transient
    @ApiModelProperty(name = "storageList",value = "储位id")
    private List<Long> storageList;

    @Transient
    @ApiModelProperty(name = "type",value = "类型：(1-修改 2-盘点登记)")
    private byte type;

    @Transient
    @ApiModelProperty(name = "materialList",value = "货品id")
    private List<Long> materialList;

    private static final long serialVersionUID = 1L;
}