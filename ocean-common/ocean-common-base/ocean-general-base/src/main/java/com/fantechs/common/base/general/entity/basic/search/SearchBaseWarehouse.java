package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseWarehouse extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -5805998375456169332L;
    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库描述
     */
    @ApiModelProperty(name = "warehouseDesc",value = "仓库描述")
    private String warehouseDesc;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 仓库类别
     */
    @ApiModelProperty(name = "warehouseCategory",value = "仓库类别")
    private String warehouseCategory;


}
