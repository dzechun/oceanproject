package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInHtPlanReceivingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/13
 */
@Data
public class WmsInHtPlanReceivingOrderDto extends WmsInHtPlanReceivingOrder implements Serializable {
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    private String warehouseName;

    @ApiModelProperty(name = "createUserName",value = "创建人")
    private String createUserName;

    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    private String modifiedUserName;

    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;
}
