package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.annotation.security.DenyAll;
import java.io.Serializable;

@Data
public class SearchWmsOutPurchaseReturnDet extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购退货单号
     */
    @ApiModelProperty(name="purchaseReturnCode",value = "采购退货单号")
    private String purchaseReturnCode;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;
}
