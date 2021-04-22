package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmSalesOrderDetDto extends OmSalesOrderDet implements Serializable {
    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName", value = "物料名称")
    private String materialName;


    /**
     * 物料版本
     */
    @ApiModelProperty(name = "materialVersion", value = "物料版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @ApiModelProperty(name = "materialDesc", value = "物料描述")
    private String materialDesc;

    /**
     * 仓库名
     */
    @ApiModelProperty(name="warehouseName", value = "仓库名称")
    private String warehouseName;


}
