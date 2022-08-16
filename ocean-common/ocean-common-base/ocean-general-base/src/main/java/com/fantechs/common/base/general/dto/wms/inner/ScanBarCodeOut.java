package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2022/6/27
 */
@Data
public class ScanBarCodeOut implements Serializable {
    @ApiModelProperty(name = "barCode", value = "条码", required = true)
    private String barCode;

    @ApiModelProperty(name = "ip", value = "ip", required = true)
    private String ip;

    @ApiModelProperty(name = "jobOrderId", value = "拣货单id", required = true)
    private Long jobOrderId;

    @ApiModelProperty(name = "platformId",value = "月台id",required = true)
    private Long platformId;
}
