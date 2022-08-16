package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2022/6/27
 */
@Data
public class WanbaoPlatformDetDto extends WanbaoPlatformDet implements Serializable {
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    private String materialCode;

    @ApiModelProperty(name = "materialName",value = "产品料号名称")
    private String materialName;

    @ApiModelProperty(name = "deliveryOrderCode",value = "出货单号")
    private String deliveryOrderCode;

    @ApiModelProperty(name = "salesOrderNo",value = "销售订单")
    private String salesOrderNo;
}
