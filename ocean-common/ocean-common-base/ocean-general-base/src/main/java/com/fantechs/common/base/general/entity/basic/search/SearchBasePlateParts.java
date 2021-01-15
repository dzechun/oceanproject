package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBasePlateParts extends BaseQuery implements Serializable {
    /**
     * 部件组成ID
     */
    @ApiModelProperty(name="platePartsId",value = "部件组成ID")
    private Long platePartsId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;



    private static final long serialVersionUID = 1L;
}
