package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MesSfcReworkOrderDto extends MesSfcReworkOrder implements Serializable {

    @ApiModelProperty(value = "routeCcode", name = "工艺路线编码")
    private String routeCcode;

    @ApiModelProperty(value = "routeName", name = "工艺路线名称")
    private String routeName;

    @ApiModelProperty(value = "processCode", name = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "processName", name = "工序名称")
    private String processName;

    @ApiModelProperty(value = "materialCode", name = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "materialName", name = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "productModelCode", name = "产品型号编码")
    private String productModelCode;

    @ApiModelProperty(value = "productModelName", name = "产品型号名称")
    private String productModelName;
}
