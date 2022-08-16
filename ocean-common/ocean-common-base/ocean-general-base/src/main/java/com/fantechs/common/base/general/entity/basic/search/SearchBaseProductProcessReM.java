package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseProductProcessReM extends BaseQuery implements Serializable {


    private static final long serialVersionUID = -1134459340423853119L;
    /**
     * 产品ID(即物料ID)
     */
    @ApiModelProperty(name="materialId",value = "产品ID(即物料ID)")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

}
