package com.fantechs.common.base.general.entity.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 装箱汇总明细
 * eng_packing_order_summary_det
 * @author 81947
 * @date 2021-08-27 09:05:45
 */
@Data
@Table(name = "eng_packing_order_summary_det")
public class EngPackingOrderSummaryDet extends ValidGroup implements Serializable {
    /**
     * 装箱汇总明细ID
     */
    @ApiModelProperty(name="packingOrderSummaryDetId",value = "装箱汇总明细ID")
    @Excel(name = "装箱汇总明细ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "packing_order_summary_det_id")
    private Long packingOrderSummaryDetId;

    /**
     * 装箱汇总ID
     */
    @ApiModelProperty(name="packingOrderSummaryId",value = "装箱汇总ID")
    @Excel(name = "装箱汇总ID", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_order_summary_id")
    private Long packingOrderSummaryId;

    /**
     * 包装箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包装箱号")
    @Excel(name = "包装箱号", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 内箱号
     */
    @ApiModelProperty(name="innerCartonCode",value = "内箱号")
    @Excel(name = "内箱号", height = 20, width = 30,orderNum="") 
    @Column(name = "inner_carton_code")
    private String innerCartonCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="") 
    @Column(name = "location_num")
    private String locationNum;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30,orderNum="") 
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 主项号
     */
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="")
    @Column(name = "dominant_term_code")
    private String dominantTermCode;


    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;


    /**
     * 图号
     */
    @ApiModelProperty(name="drawingNumber",value = "图号")
    @Excel(name = "图号", height = 20, width = 30,orderNum="") 
    @Column(name = "drawing_number")
    private String drawingNumber;

    /**
     * 件号
     */
    @ApiModelProperty(name="partNumber",value = "件号")
    @Excel(name = "件号", height = 20, width = 30,orderNum="") 
    @Column(name = "part_number")
    private String partNumber;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="") 
    private BigDecimal qty;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="") 
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 原材料编码
     */
    @ApiModelProperty(name="rawMaterialCode",value = "原材料编码")
    @Excel(name = "原材料编码", height = 20, width = 30,orderNum="") 
    @Column(name = "raw_material_code")
    private String rawMaterialCode;


    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="")
    @Column(name = "despatch_batch")
    private String despatchBatch;

    /**
     * 规格
     */
    @ApiModelProperty(name="spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="")
    @Column(name = "spec")
    private String spec;


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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 分配数量
     */
    @ApiModelProperty(value = "分配数量",name = "distributionQty")
    @Column(name = "distribution_qty")
    private BigDecimal distributionQty;

    /**
     * 上架数量
     */
    @ApiModelProperty( value = "上架数量",name = "putawayQty")
    @Column(name = "putaway_qty")
    private BigDecimal putawayQty;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量",name = "receivingQty")
    @Column(name = "receiving_qty")
    private BigDecimal receivingQty;

    @ApiModelProperty(value = "收货库位id",name = "receivingStorageId")
    @Column(name = "receiving_storage_id")
    private Long receivingStorageId;


    @ApiModelProperty(name = "putawayStorageId",value = "上架库位")
    @Column(name = "putaway_storage_id")
    private Long putawayStorageId;

    /**
     *汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)
     */
    @ApiModelProperty(name = "summaryDetStatus",value = "汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)")
    @Column(name = "summary_det_status")
    private Byte summaryDetStatus;

    private static final long serialVersionUID = 1L;
}