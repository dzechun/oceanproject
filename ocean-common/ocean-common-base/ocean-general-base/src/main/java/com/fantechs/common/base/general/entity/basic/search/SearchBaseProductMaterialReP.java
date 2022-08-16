package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseProductMaterialReP extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -9079149278461927354L;
    /**
     * 物料工序关系表ID
     */
    @ApiModelProperty(name="productProcessReMId",value = "物料工序关系表ID")
    private Long productProcessReMId;

}
