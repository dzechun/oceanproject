package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmPurchaseOrderDetDto extends OmPurchaseOrderDet implements Serializable {

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;
}
