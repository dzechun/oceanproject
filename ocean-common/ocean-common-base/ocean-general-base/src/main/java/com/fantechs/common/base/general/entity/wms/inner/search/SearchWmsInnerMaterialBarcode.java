package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchWmsInnerMaterialBarcode extends BaseQuery implements Serializable {

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    /**
     * 打印单据类型
     */
    @ApiModelProperty(name="printOrderTypeCode",value = "打印单据类型")
    private String printOrderTypeCode;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="printOrderDetId",value = "来料条码ID")
    private Long printOrderDetId;

    /**
     * 打印单据编码
     */
    @ApiModelProperty(name="printOrderCode",value = "打印单据编码")
    private String printOrderCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 来料条码ID列表
     */
    @ApiModelProperty(name="materialBarcodeIdList",value = "来料条码ID列表")
    private List<Long> materialBarcodeIdList;

    /**
     * 是否系统条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码(0-否 1-是)")
    private Byte ifSysBarcode;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    /**
     * 生产时间开始
     */
    @ApiModelProperty(name="productionTimeStart",value = "生产时间")
    private String productionTimeStart;

    /**
     * 生产时间结束
     */
    @ApiModelProperty(name="productionTimeEnd",value = "生产时间结束")
    private String productionTimeEnd;

    /**
     * (未知条码)
     */
    @ApiModelProperty(name="barcode",value = "未知条码")
    private String code;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    private static final long serialVersionUID = 1L;
}
