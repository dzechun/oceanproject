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
 * 初始化盘点明细
 * wms_inner_init_stock_det
 * @author mr.lei
 * @date 2021-12-01 10:02:20
 */
@Data
@Table(name = "wms_inner_init_stock_det")
public class WmsInnerInitStockDet extends ValidGroup implements Serializable {
    /**
     * 初始化盘点明细id
     */
    @ApiModelProperty(name="initStockDetId",value = "初始化盘点明细id")
    @Excel(name = "初始化盘点明细id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "init_stock_det_id")
    private Long initStockDetId;

    @Column(name = "init_stock_id")
    private Long initStockId;

    /**
     * 1-国内 2-GE\多美达 3-海外 4-三星
     */
    @ApiModelProperty(name = "product_type",value = "产品类型（1-国内 2-GE/多美达 3-海外 4-三星")
    @Column(name = "product_type")
    private Byte productType;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Excel(name = "物料id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 盘点数量
     */
    @ApiModelProperty(name="stockQty",value = "盘点数量")
    @Excel(name = "盘点数量", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_qty")
    private BigDecimal stockQty;

    /**
     * 差异数量
     */
    @ApiModelProperty(name="varianceQty",value = "差异数量")
    @Excel(name = "差异数量", height = 20, width = 30,orderNum="") 
    @Column(name = "variance_qty")
    private BigDecimal varianceQty;

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

    @Transient
    private List<WmsInnerInitStockBarcode> wmsInnerInitStockBarcodes;

    private static final long serialVersionUID = 1L;
}