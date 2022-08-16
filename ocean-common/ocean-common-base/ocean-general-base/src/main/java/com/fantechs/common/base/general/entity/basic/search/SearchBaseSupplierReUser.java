package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseSupplierReUser extends BaseQuery implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId",value = "用户ID")
    private Long userId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;
}
