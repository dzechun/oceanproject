package com.fantechs.common.base.general.entity.wms.inner;

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
import java.util.List;

;
;

/**
 * 初始化盘点 
 * wms_inner_init_stock
 * @author mr.lei
 * @date 2021-12-01 10:02:20
 */
@Data
@Table(name = "wms_inner_init_stock")
public class WmsInnerInitStock extends ValidGroup implements Serializable {
    /**
     * 初始化盘点id
     */
    @ApiModelProperty(name="initStockId",value = "初始化盘点id")
    @Id
    @Column(name = "init_stock_id")
    private Long initStockId;

    /**
     * 初始化盘点单号
     */
    @ApiModelProperty(name="initStockOrderCode",value = "初始化盘点单号")
    @Excel(name = "初始化盘点单号", height = 20, width = 30,orderNum="1")
    @Column(name = "init_stock_order_code")
    private String initStockOrderCode;

    /**
     * 库位
     */
    @ApiModelProperty(name="storageId",value = "库位")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 盘点类型：1-初始化盘点，2-正常盘点
     */
    @ApiModelProperty(name="initStockType",value = "盘点类型：1-初始化盘点，2-正常盘点")
    @Excel(name = "盘点类型：1-初始化盘点，2-正常盘点", height = 20, width = 30,orderNum="5")
    @Column(name = "init_stock_type")
    private Byte initStockType;

    /**
     * 单据状态(1-打开 2-待作业 3-作业中 4-作废 5-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待盘点 2-盘点中 3-已盘点)")
    @Excel(name = "单据状态(1-待盘点 2-已盘点)", height = 20, width = 30,orderNum="9")
    @Column(name = "order_status")
    private Byte orderStatus;


    /**
     * 计划数量
     */
    @ApiModelProperty(name = "totalPlanQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="6")
    @Column(name = "total_plan_qty")
    private BigDecimal totalPlanQty;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    @Transient
    private List<WmsInnerInitStockDet> wmsInnerInitStockDets;

    private static final long serialVersionUID = 1L;
}