package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OmSalesOrderDto extends OmSalesOrder implements Serializable {
    /**
     * 客户名
     */
    @ApiModelProperty(name="supplierName", value="客户名")
    private String supplierName;

    /**
     * 条码规则集合名称
     */
    @ApiModelProperty(name="barcodeRuleSetName", value = "条码规则集合名称")
    private String barcodeRuleSetName;

    /**
     * 表体相关
     */
    @ApiModelProperty(name="omSalesOrderDetDtoList", value="表体")
    private List<OmSalesOrderDetDto> omSalesOrderDetDtoList;

}
