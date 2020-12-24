package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsOutFinishedProductDet extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成品出库单号
     */
    @ApiModelProperty(name="finishedProductCode",value = "成品出库单号")
    private String finishedProductCode;

    @ApiModelProperty(name="productMaterialCode" ,value="成品编码")
    private String productMaterialCode;

    @ApiModelProperty(name="productMaterialName" ,value="成品名称")
    private String productMaterialName;

    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;




}
