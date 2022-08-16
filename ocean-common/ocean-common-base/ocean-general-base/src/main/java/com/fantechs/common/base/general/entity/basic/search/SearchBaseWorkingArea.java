package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBaseWorkingArea extends BaseQuery implements Serializable {


    /**
     * 工作区域编码
     */
    @ApiModelProperty(name="workingAreaCode",value = "工作区域编码")
    private String workingAreaCode;

    /**
     * 库区名称
     */
    @ApiModelProperty(name="warehouseAreaName",value = "库区名称")
    private String warehouseAreaName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Byte status;

    /**
     * 库区id
     */
    @ApiModelProperty(name="warehouseAreaId",value = "库区id")
    private Long warehouseAreaId;

}
