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

/**
 * 来料条码表
 * wms_inner_material_barcode
 * @author 81947
 * @date 2021-07-03 14:29:25
 */
@Data
@Table(name = "wms_inner_material_barcode")
public class WmsInnerMaterialBarcode extends ValidGroup implements Serializable {
    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    @Excel(name = "来料条码ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 产生类型(1-供应商条码 2-自己打印 3-生产条码)
     */
    @ApiModelProperty(name="createType",value = "产生类型(1-供应商条码 2-自己打印 3-生产条码)")
    @Excel(name = "产生类型(1-供应商条码 2-自己打印 3-生产条码)", height = 20, width = 30,orderNum="")
    @Column(name = "create_type")
    private Byte createType;

    /**
     * 打印指来源端IP
     */
    @ApiModelProperty(name="printOrderSourceIp",value = "打印指来源端IP")
    @Excel(name = "打印指来源端IP", height = 20, width = 30,orderNum="")
    @Column(name = "print_order_source_ip")
    private String printOrderSourceIp;

    /**
     * 打印机名称
     */
    @ApiModelProperty(name="printerName",value = "打印机名称")
    @Excel(name = "打印机名称", height = 20, width = 30,orderNum="")
    @Column(name = "printer_name")
    private String printerName;

    /**
     * 打印单据类型
     */
    @ApiModelProperty(name="printOrderTypeCode",value = "打印单据类型")
    @Excel(name = "打印单据类型", height = 20, width = 30,orderNum="")
    @Column(name = "print_order_type_code")
    private String printOrderTypeCode;

    /**
     * 打印单据编码
     */
    @ApiModelProperty(name="printOrderCode",value = "打印单据编码")
    @Excel(name = "打印单据编码", height = 20, width = 30,orderNum="")
    @Column(name = "print_order_code")
    private String printOrderCode;

    /**
     * 打印单据ID
     */
    @ApiModelProperty(name="printOrderId",value = "打印单据ID")
    @Excel(name = "打印单据ID", height = 20, width = 30,orderNum="")
    @Column(name = "print_order_id")
    private Long printOrderId;

    /**
     * 打印单据明细ID
     */
    @ApiModelProperty(name="printOrderDetId",value = "打印单据明细ID")
    @Excel(name = "打印单据明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "print_order_det_id")
    private Long printOrderDetId;

    /**
     * 条码（SN码）
     */
    @ApiModelProperty(name="barcode",value = "条码（SN码）")
    @Excel(name = "条码（SN码）", height = 20, width = 30,orderNum="")
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="")
    @Column(name = "color_box_code")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    @Excel(name = "箱号", height = 20, width = 30,orderNum="")
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30,orderNum="")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 非系统条码
     */
    @ApiModelProperty(name="notSysCode",value = "非系统条码")
    @Excel(name = "非系统条码", height = 20, width = 30,orderNum="")
    @Column(name = "not_sys_code")
    private String notSysCode;

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
     * 生产时间
     */
    @ApiModelProperty(name="productionTime",value = "生产时间")
    @Excel(name = "生产时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "production_time")
    private Date productionTime;

    /**
     * 1-未质检 2-合格 3-不合格
     */
    @ApiModelProperty(name="inspectionStatus",value = "1-未质检 2-合格 3-不合格")
    @Excel(name = "1-未质检 2-合格 3-不合格", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_status")
    private Byte inspectionStatus;

    /**
     * 条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
     */
    @ApiModelProperty(name="barcodeStatus",value = "条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)")
    @Excel(name = "条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)", height = 20, width = 30,orderNum="")
    @Column(name = "barcode_status")
    private Byte barcodeStatus;

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
