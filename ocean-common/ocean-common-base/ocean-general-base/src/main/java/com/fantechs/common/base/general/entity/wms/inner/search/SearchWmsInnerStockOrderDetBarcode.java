package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/27
 */
@Data
public class SearchWmsInnerStockOrderDetBarcode extends BaseQuery implements Serializable {

    /**
     * 盘点单明细条码表ID
     */
    @ApiModelProperty(name="stockOrderDetBarcodeId",value = "盘点单明细条码表ID")
    private Long stockOrderDetBarcodeId;

    /**
     * 盘点单ID
     */
    @ApiModelProperty(name="stockOrderId",value = "盘点单ID")
    private Long stockOrderId;

    /**
     * 盘点单明细ID
     */
    @ApiModelProperty(name="stockOrderDetId",value = "盘点单明细ID")
    private Long stockOrderDetId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    /**
     * 条码SN
     */
    @ApiModelProperty(name="barcode",value = "条码SN")
    private String barcode;

    /**
     * 彩盒码
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒码")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    private String cartonCode;

    /**
     * 栈板
     */
    @ApiModelProperty(name="palletCode",value = "栈板")
    private String palletCode;

    /**
     * 扫描状态(1-未扫描 2-已保存 3-已提交)
     */
    @ApiModelProperty(name="scanStatus",value = "扫描状态(1-未扫描 2-已保存 3-已提交)")
    private Byte scanStatus;

    /**
     * 盘点结果(1-盘点、2-已盘点、3-盘盈、4-盘亏)
     */
    @ApiModelProperty(name="stockResult",value = "盘点结果(1-盘点、2-已盘点、3-盘盈、4-盘亏)")
    private Byte stockResult;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

}