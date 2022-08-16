package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class WmsInnerInitStockDetDto extends WmsInnerInitStockDet implements Serializable {

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    @ApiModelProperty(name = "createUserName",value = "创建用户")
    private String createUserName;

    @ApiModelProperty(name = "modifiedUserName",value = "修改用户")
    private String modifiedUserName;

    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
