package com.fantechs.common.base.general.dto.wms.out;

import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsOutPurchaseReturnDto extends WmsOutPurchaseReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商代码
     */
    @ApiModelProperty(name="customerCode" ,value="供应商代码")
    private String customerCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="customerName" ,value="供应商名称")
    private String customerName;

    /**
     * 采购员名称
     */
    @ApiModelProperty(name="buyName" ,value="采购员名称")
    private String buyName;

}
