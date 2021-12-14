package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/14
 */
@Data
public class WmsInReceivingOrderDetDto extends WmsInReceivingOrderDet implements Serializable {
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    @ApiModelProperty(name = "createUserName",value = "创建人")
    private String createUserName;

    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    private String modifiedUserName;

    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;

    @ApiModelProperty(name = "operatorUserName",value = "操作人")
    private String operatorUserName;
}
