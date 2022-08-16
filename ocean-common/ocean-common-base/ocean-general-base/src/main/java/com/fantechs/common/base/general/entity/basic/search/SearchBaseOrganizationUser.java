package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseOrganizationUser extends BaseQuery implements Serializable {

    /**
     * 用户id
     */
    @ApiModelProperty(name="userId",value = "用户id")
    private Long userId;
}
