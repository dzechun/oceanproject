package com.fantechs.common.base.general.entity.wms.inner;

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
 * 来料条码表履历表
 * wms_inner_ht_material_barcode
 * @author 81947
 * @date 2021-07-03 14:29:26
 */
@Data
@Table(name = "wms_inner_ht_material_barcode")
public class WmsInnerHtMaterialBarcode extends ValidGroup implements Serializable {
    /**
     * 来料条码履历ID
     */
    @ApiModelProperty(name="htMaterialBarcodeId",value = "来料条码履历ID")
    @Excel(name = "来料条码履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_material_barcode_id")
    private Long htMaterialBarcodeId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    @Excel(name = "来料条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="") 
    private String barcode;

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
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    @Excel(name = "批号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    @Excel(name = "物料数量", height = 20, width = 30,orderNum="") 
    @Column(name = "material_qty")
    private BigDecimal materialQty;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="") 
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Excel(name = "条码规则ID", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_rule_id")
    private Long barcodeRuleId;

    /**
     * 标签信息ID
     */
    @ApiModelProperty(name="labelId",value = "标签信息ID")
    @Excel(name = "标签信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "label_id")
    private Long labelId;

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