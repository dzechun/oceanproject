package com.fantechs.common.base.general.entity.srm.history;

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
 * 装箱汇总履历表
 * srm_ht_packing_order_summary
 * @author 81947
 * @date 2021-08-27 09:05:45
 */
@Data
@Table(name = "srm_ht_packing_order_summary")
public class SrmHtPackingOrderSummary extends ValidGroup implements Serializable {
    /**
     * 装箱汇总履历ID
     */
    @ApiModelProperty(name="htPackingOrderSummaryId",value = "装箱汇总履历ID")
    @Excel(name = "装箱汇总履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_packing_order_summary_id")
    private Long htPackingOrderSummaryId;

    /**
     * 装箱汇总ID
     */
    @ApiModelProperty(name="packingOrderSummaryId",value = "装箱汇总ID")
    @Excel(name = "装箱汇总ID", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_order_summary_id")
    private Long packingOrderSummaryId;

    /**
     * 装箱单ID
     */
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    @Excel(name = "装箱单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_order_id")
    private Long packingOrderId;

    /**
     * 请购单号
     */
    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    @Excel(name = "请购单号", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_req_order_code")
    private String purchaseReqOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="") 
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 包装箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包装箱号")
    @Excel(name = "包装箱号", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30,orderNum="") 
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="") 
    @Column(name = "location_num")
    private String locationNum;

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
     * 箱数
     */
    @ApiModelProperty(name="cartonQty",value = "箱数")
    @Excel(name = "箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_qty")
    private Integer cartonQty;

    /**
     * 包装方式
     */
    @ApiModelProperty(name="packingType",value = "包装方式")
    @Excel(name = "包装方式", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_type")
    private String packingType;

    /**
     * 净重(KG)
     */
    @ApiModelProperty(name="netWeight",value = "净重(KG)")
    @Excel(name = "净重(KG)", height = 20, width = 30,orderNum="") 
    @Column(name = "net_weight")
    private BigDecimal netWeight;

    /**
     * 毛重(KG)
     */
    @ApiModelProperty(name="grossWeight",value = "毛重(KG)")
    @Excel(name = "毛重(KG)", height = 20, width = 30,orderNum="") 
    @Column(name = "gross_weight")
    private BigDecimal grossWeight;

    /**
     * 长(cm)
     */
    @ApiModelProperty(name="length",value = "长(cm)")
    @Excel(name = "长(cm)", height = 20, width = 30,orderNum="") 
    private BigDecimal length;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    @Excel(name = "高(cm)", height = 20, width = 30,orderNum="") 
    private BigDecimal height;

    /**
     * 体积(m3)
     */
    @ApiModelProperty(name="volume",value = "体积(m3)")
    @Excel(name = "体积(m3)", height = 20, width = 30,orderNum="") 
    private BigDecimal volume;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    @Excel(name = "宽(cm)", height = 20, width = 30,orderNum="") 
    private BigDecimal width;

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

    private static final long serialVersionUID = 1L;
}