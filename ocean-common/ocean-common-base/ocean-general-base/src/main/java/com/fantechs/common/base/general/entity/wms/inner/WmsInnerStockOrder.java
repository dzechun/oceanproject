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
 * 库存盘点
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
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Excel(name = "核心系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Excel(name = "系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    @Excel(name = "来源大类(1-系统下推 2-自建 3-第三方系统)", height = 20, width = 30,orderNum="")
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 盘点单号--
     */
    @ApiModelProperty(name="planStockOrderCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="1")
    @Column(name = "plan_stock_order_code")
    private String planStockOrderCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="3")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 盘点类型(1-物料 2-库位 3-全盘)--
     */
    @ApiModelProperty(name="stockType",value = "盘点类型(1-物料 2-库位 3-全盘)")
    @Excel(name = "盘点类型", height = 20, width = 30,orderNum="4",replace = {"盘点类型(1-物料 2-库位 3-全盘)"})
    @Column(name = "stock_type")
    private Byte stockType;

    /**
     * 盘点或复盘(1-盘点 2-复盘)
     */
    @ApiModelProperty(name="projectType",value = "盘点或复盘(1-盘点 2-复盘)")
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
     * 单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)")
    @Excel(name = "单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)", height = 20, width = 30,orderNum="")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 盘点方式(1-PDA盘点 2-纸质盘点)
     */
    @ApiModelProperty(name="stockMode",value = "盘点方式(1-PDA盘点 2-纸质盘点)")
    @Excel(name = "盘点方式(1-PDA盘点 2-纸质盘点)", height = 20, width = 30,orderNum="")
    @Column(name = "stock_mode")
    private Byte stockMode;

    /**
     * 是否盲盘(0-否 1-是)
     */
    @ApiModelProperty(name="ifBlindStock",value = "是否盲盘(0-否 1-是)")
    @Column(name = "if_blind_stock")
    private Byte ifBlindStock;

    /**
     * 最大库位数
     */
    @ApiModelProperty(name="maxStorageCount",value = "最大库位数")
    @Excel(name = "最大库位数", height = 20, width = 30,orderNum="")
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
    private List<WmsInnerStockOrderDet> wmsInnerStockOrderDets;

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