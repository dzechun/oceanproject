package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamSparePart extends BaseQuery implements Serializable {
    /**
     * 备用件编码
     */
    @ApiModelProperty(name="sparePartCode",value = "备用件编码")
    private String sparePartCode;

    /**
     * 备用件名稱
     */
    @ApiModelProperty(name="sparePartName",value = "备用件名稱")
    private String sparePartName;

    /**
     * 备用件描述
     */
    @ApiModelProperty(name="sparePartDesc",value = "备用件描述")
    private String sparePartDesc;

    /**
     * 备用件型號
     */
    @ApiModelProperty(name="sparePartModel",value = "备用件型號")
    private String sparePartModel;


}
