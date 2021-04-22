package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmHtSalesOrderDto extends OmHtSalesOrder implements Serializable {
    @ApiModelProperty(name = "supplierName", value = "客户名称")
    private String supplierName;
    @ApiModelProperty(name = "barcodeRuleSetName", value = "条码规则集合名称")
    private String barcodeRuleSetName;
}
