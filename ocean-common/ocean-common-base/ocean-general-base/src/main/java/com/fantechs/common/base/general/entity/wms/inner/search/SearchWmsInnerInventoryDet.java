package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class SearchWmsInnerInventoryDet extends BaseQuery implements Serializable {
    @ApiModelProperty("库位id")
    private Long storageId;
    @ApiModelProperty("物料id")
    private Long materialId;
    @ApiModelProperty("条码")
    private String barcode;
    @ApiModelProperty("相关单号")
    private String relevanceOrderCode;
    @ApiModelProperty("物料数量")
    private BigDecimal materialQty;
    @ApiModelProperty("是否是不相等 -- 0、相等 1、不相等")
    private Integer notEqualMark;

    @ApiModelProperty("作业状态")
    private Byte jobStatus;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("库位")
    private String storageCode;

    @ApiModelProperty("库存状态id")
    private Long inventoryStatusId;

    @ApiModelProperty("检验单编码")
    private String inspectionOrderCode;

    @ApiModelProperty("检验单编码是否为空  0-否 1-是")
    private Integer ifInspectionOrderCodeNull;
}
