package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSrmCarport extends BaseQuery implements Serializable {

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "organizationName",value = "仓库名称")
    private String warehouseName;

    private static final long serialVersionUID = 1L;
}
