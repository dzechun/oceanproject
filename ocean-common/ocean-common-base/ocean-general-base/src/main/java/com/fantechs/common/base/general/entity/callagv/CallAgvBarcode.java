package com.fantechs.common.base.general.entity.callagv;

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

/**
 * 条码表
 * call_agv_barcode
 * @author 86178
 * @date 2021-09-15 16:31:56
 */
@Data
@Table(name = "call_agv_barcode")
public class CallAgvBarcode extends ValidGroup implements Serializable {
    /**
     * 条码表ID
     */
    @ApiModelProperty(name="barcodeId",value = "条码表ID")
    @Id
    @Column(name = "barcode_id")
    private Long barcodeId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="1")
    private String barcode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="4")
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 型号
     */
    @ApiModelProperty(name="productModel",value = "型号")
    @Excel(name = "型号", height = 20, width = 30,orderNum="2")
    @Column(name = "product_model")
    private String productModel;

    /**
     * ERP型号
     */
    @ApiModelProperty(name="erpProductModel",value = "ERP型号")
    @Excel(name = "ERP型号", height = 20, width = 30,orderNum="3")
    @Column(name = "erp_product_model")
    private String erpProductModel;

    /**
     * 批次
     */
    @ApiModelProperty(name="batch",value = "批次")
    @Excel(name = "批次", height = 20, width = 30,orderNum="5")
    private String batch;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    @Excel(name = "批号", height = 20, width = 30,orderNum="6")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="7")
    private BigDecimal qty;

    /**
     * 作业员
     */
    @ApiModelProperty(name="operatorUserName",value = "作业员")
    @Excel(name = "作业员", height = 20, width = 30,orderNum="8")
    @Column(name = "operator_user_name")
    private String operatorUserName;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="9")
    @Column(name = "process_name")
    private String processName;

    /**
     * 保质期
     */
    @ApiModelProperty(name="shelfLife",value = "保质期")
    @Excel(name = "保质期", height = 20, width = 30,orderNum="10")
    @Column(name = "shelf_life")
    private String shelfLife;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="11")
    @Column(name = "production_date")
    private String productionDate;

    /**
     * 流水号
     */
    @ApiModelProperty(name="serialNumber",value = "流水号")
    @Excel(name = "流水号", height = 20, width = 30,orderNum="12")
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * 注塑机名称
     */
    @ApiModelProperty(name="zhuSuJiName",value = "注塑机名称")
    @Excel(name = "注塑机名称", height = 20, width = 30,orderNum="13")
    @Column(name = "zhu_su_ji_name")
    private String zhuSuJiName;

    /**
     * 注塑机班次
     */
    @ApiModelProperty(name="zhuSuJiShift",value = "注塑机班次")
    @Excel(name = "注塑机班次", height = 20, width = 30,orderNum="14")
    @Column(name = "zhu_su_ji_shift")
    private String zhuSuJiShift;

    /**
     * 折弯机名称
     */
    @ApiModelProperty(name="zheWanJiName",value = "折弯机名称")
    @Excel(name = "折弯机名称", height = 20, width = 30,orderNum="15")
    @Column(name = "zhe_wan_ji_name")
    private String zheWanJiName;

    /**
     * 折弯机班次
     */
    @ApiModelProperty(name="zheWanJiShift",value = "折弯机班次")
    @Excel(name = "折弯机班次", height = 20, width = 30,orderNum="16")
    @Column(name = "zhe_wan_ji_shift")
    private String zheWanJiShift;

    /**
     * 材料厂家
     */
    @ApiModelProperty(name="materialFactory",value = "材料厂家")
    @Excel(name = "材料厂家", height = 20, width = 30,orderNum="17")
    @Column(name = "material_factory")
    private String materialFactory;

    /**
     * 材料规格
     */
    @ApiModelProperty(name="materialSpec",value = "材料规格")
    @Excel(name = "材料规格", height = 20, width = 30,orderNum="18")
    @Column(name = "material_spec")
    private String materialSpec;

    /**
     * 条码状态(1-待入库 2-已备料 3-已入库 4-已出库 5-已解绑)
     */
    @ApiModelProperty(name="barcodeStatus",value = "条码状态(1-待入库 2-已备料 3-已入库 4-已出库 5-已解绑)")
    @Column(name = "barcode_status")
    private Byte barcodeStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="20",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="22",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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