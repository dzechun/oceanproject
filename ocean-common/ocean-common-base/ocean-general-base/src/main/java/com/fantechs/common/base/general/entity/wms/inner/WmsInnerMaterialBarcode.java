package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Id
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 产生类型(1-供应商条码 2-自己打印 3-生产条码)
     */
    @ApiModelProperty(name="createType",value = "产生类型(1-供应商条码 2-自己打印 3-生产条码)")
    @Column(name = "create_type")
    private Byte createType;

    /**
     * 条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)
     */
    @ApiModelProperty(name="barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)")
    @Column(name = "barcode_type")
    private Byte barcodeType;

    /**
     * 打印指来源端IP
     */
    @ApiModelProperty(name="printOrderSourceIp",value = "打印指来源端IP")
    @Column(name = "print_order_source_ip")
    private String printOrderSourceIp;

    /**
     * 打印机名称
     */
    @ApiModelProperty(name="printerName",value = "打印机名称")
    @Column(name = "printer_name")
    private String printerName;

    /**
     * 打印单据类型
     */
    @ApiModelProperty(name="printOrderTypeCode",value = "打印单据类型")
    @Column(name = "print_order_type_code")
    private String printOrderTypeCode;

    /**
     * 打印单据编码
     */
    @ApiModelProperty(name="printOrderCode",value = "打印单据编码")
    @Excel(name = "单号", height = 20, width = 30,orderNum="1")
    @Column(name = "print_order_code")
    private String printOrderCode;

    /**
     * 打印单据ID
     */
    @ApiModelProperty(name="printOrderId",value = "打印单据ID")
    @Column(name = "print_order_id")
    private Long printOrderId;

    /**
     * 打印单据明细ID
     */
    @ApiModelProperty(name="printOrderDetId",value = "打印单据明细ID")
    @Column(name = "print_order_det_id")
    private Long printOrderDetId;

    /**
     * 条码（SN码）
     */
    @ApiModelProperty(name="barcode",value = "条码（SN码）")
    @Excel(name = "条码（SN码）", height = 20, width = 30,orderNum="5")
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="6")
    @Column(name = "color_box_code")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    @Excel(name = "箱号", height = 20, width = 30,orderNum="7")
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30,orderNum="8")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 是否系统条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码(0-否 1-是)")
    @Column(name = "if_sys_barcode")
    @Excel(name = "是否系统条码", height = 20, width = 30,orderNum="9",replace = {"否_0","是_1"})
    private Byte ifSysBarcode;

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
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    @Excel(name = "批号", height = 20, width = 30,orderNum="10")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    @Excel(name = "物料数量", height = 20, width = 30,orderNum="12")
    @Column(name = "material_qty")
    private BigDecimal materialQty;

    /**
     * 生产时间
     */
    @ApiModelProperty(name="productionTime",value = "生产时间")
    @Excel(name = "生产时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "production_time")
    private Date productionTime;

    /**
     * 过期时间
     */
    @ApiModelProperty(name="expiredTime",value = "过期时间")
//    @Excel(name = "过期时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expired_time")
    private Date expiredTime;


    /**
     * 1-未质检 2-合格 3-不合格
     */
    @ApiModelProperty(name="inspectionStatus",value = "1-未质检 2-合格 3-不合格")
    @Column(name = "inspection_status")
    private Byte inspectionStatus;

    /**
     * 条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
     */
    @ApiModelProperty(name="barcodeStatus",value = "条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)")
    @Column(name = "barcode_status")
    private Byte barcodeStatus;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Column(name = "barcode_rule_id")
    private Long barcodeRuleId;

    /**
     * 标签信息ID
     */
    @ApiModelProperty(name="labelId",value = "标签信息ID")
    @Column(name = "label_id")
    private Long labelId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}
