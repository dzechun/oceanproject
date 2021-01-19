package com.fantechs.common.base.general.entity.qms.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchQmsBadItemDet extends BaseQuery implements Serializable {


    /**
     * 不良项目ID
     */
    @ApiModelProperty(name="badItemId",value = "不良项目ID")
    private Long badItemId;

    /**
     * 不良现象
     */
    @ApiModelProperty(name="badPhenomenon",value = "不良现象")
    private String badPhenomenon;

    private static final long serialVersionUID = 1L;
}
