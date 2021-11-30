package com.fantechs.common.base.general.entity.srm;

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
 * ASN单明细条码
 * wms_in_asn_order_det_barcode
 * @author 81947
 * @date 2021-11-25 09:19:11
 */
@Data
@Table(name = "wms_in_asn_order_det_barcode")
public class SrmInAsnOrderDetBarcode extends ValidGroup implements Serializable {
    /**
     * ASN单明细条码ID
     */
    @ApiModelProperty(name="asnOrderDetBarcodeId",value = "ASN单明细条码ID")
    @Excel(name = "ASN单明细条码ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "asn_order_det_barcode_id")
    private Long asnOrderDetBarcodeId;

    /**
     * ASN单ID
     */
    @ApiModelProperty(name="asnOrderId",value = "ASN单ID")
    @Excel(name = "ASN单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "asn_order_id")
    private Long asnOrderId;

    /**
     * ASN单明细ID
     */
    @ApiModelProperty(name="asnOrderDetId",value = "ASN单明细ID")
    @Excel(name = "ASN单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "asn_order_det_id")
    private Long asnOrderDetId;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="") 
    private BigDecimal qty;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="") 
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * SN码
     */
    @ApiModelProperty(name="barcode",value = "SN码")
    @Excel(name = "SN码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="") 
    @Column(name = "color_box_code")
    private String colorBoxCode;

    /**
     * 箱码
     */
    @ApiModelProperty(name="cartonCode",value = "箱码")
    @Excel(name = "箱码", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 栈板码
     */
    @ApiModelProperty(name="palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30,orderNum="") 
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}