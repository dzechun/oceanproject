package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class SearchSmtMaterialPackage extends BaseQuery {

    /**
     * 包装规格ID
     */
    @ApiModelProperty(name="packageSpecificationId",value = "包装规格ID")
    private Long packageSpecificationId;
}
