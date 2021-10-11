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
    @Id
    @Column(name = "packing_order_summary_det_id")
    private Long packingOrderSummaryDetId;

    /**
     * 装箱汇总ID
     */
    @ApiModelProperty(name="packingOrderSummaryId",value = "装箱汇总ID")
    @Column(name = "packing_order_summary_id")
    private Long packingOrderSummaryId;

    /**
     * 包装箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包装箱号")
    @Excel(name = "包装箱号", height = 20, width = 30,orderNum="7")
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 内箱号
     */
    @ApiModelProperty(name="innerCartonCode",value = "内箱号")
    @Excel(name = "内箱号", height = 20, width = 30,orderNum="8")
    @Column(name = "inner_carton_code")
    private String innerCartonCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="9")
    @Column(name = "location_num")
    private String locationNum;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30,orderNum="11")
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 主项号
     */
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="10")
    @Column(name = "dominant_term_code")
    private String dominantTermCode;


    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;


    /**
     * 图号
     */
    @ApiModelProperty(name="drawingNumber",value = "图号")
    @Excel(name = "图号", height = 20, width = 30,orderNum="13")
    @Column(name = "drawing_number")
    private String drawingNumber;

    /**
     * 件号
     */
    @ApiModelProperty(name="partNumber",value = "件号")
    @Excel(name = "件号", height = 20, width = 30,orderNum="14")
    @Column(name = "part_number")
    private String partNumber;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
   // @Excel(name = "数量", height = 20, width = 30,orderNum="17")
    private BigDecimal qty;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位", height = 20, width = 30,orderNum="18")
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 原材料编码
     */
    @ApiModelProperty(name="rawMaterialCode",value = "原材料编码")
   // @Excel(name = "原材料编码", height = 20, width = 30,orderNum="18")
    @Column(name = "raw_material_code")
    private String rawMaterialCode;


    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    @Column(name = "despatch_batch")
    private String despatchBatch;

    /**
     * 规格
     */
    @ApiModelProperty(name="spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="16")
    @Column(name = "spec")
    private String spec;


    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="19")
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