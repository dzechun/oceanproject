package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/8/27
 */
@Data
public class SearchWmsInnerBarcodeOperation extends BaseQuery implements Serializable {

    /**
     * 条码操作表ID
     */
    @ApiModelProperty(name="barcodeOperationId",value = "条码操作表ID")
    private Long barcodeOperationId;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 替换条码
     */
    @ApiModelProperty(name="replaceBarcode",value = "替换条码")
    private String replaceBarcode;

    /**
     * 出货口
     */
    @ApiModelProperty(name="outPort",value = "出货口")
    private String outPort;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="operationType",value = "单据类型")
    private String operationType;
}
