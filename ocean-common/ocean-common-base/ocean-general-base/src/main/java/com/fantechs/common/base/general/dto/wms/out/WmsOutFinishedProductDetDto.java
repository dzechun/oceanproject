package com.fantechs.common.base.general.dto.wms.out;

import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsOutFinishedProductDetDto extends WmsOutFinishedProductDet implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(name="warehouseUserName" ,value="仓库管理员名称")
    private String warehouseUserName;
}
