package com.fantechs.provider.lizi.entity;

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
 * 粟子条码表
 * lizi_scan_barcode_log
 * @author mr.lei
 * @date 2021-12-16 15:34:02
 */
@Data
@Table(name = "lizi_scan_barcode_log")
public class LiziScanBarcodeLog extends ValidGroup implements Serializable {
    /**
     * 扫描条码表ID
     */
    @ApiModelProperty(name="scanBarcodeLogId",value = "扫描条码表ID")
    @Id
    @Column(name = "scan_barcode_log_id")
    private Long scanBarcodeLogId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "SN", height = 20, width = 30,orderNum="7")
    private String barcode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="1")
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="2")
    @Column(name = "material_name")
    private String materialName;

    /**
     * 型号
     */
    @ApiModelProperty(name="productModel",value = "型号")
    @Column(name = "product_model")
    private String productModel;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Column(name = "production_date")
    private String productionDate;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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