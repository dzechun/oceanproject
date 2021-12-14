package com.fantechs.common.base.general.dto.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/13
 */
@Data
public class WmsInReceivingOrderDto extends WmsInReceivingOrder implements Serializable {
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="5")
    private String warehouseName;

    @ApiModelProperty(name = "createUserName",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum="8")
    private String createUserName;

    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;
}
