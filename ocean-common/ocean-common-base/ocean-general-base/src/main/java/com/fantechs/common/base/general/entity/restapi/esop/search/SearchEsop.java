package com.fantechs.common.base.general.entity.restapi.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEsop extends BaseQuery implements Serializable {
    /**
     * 部门编码
     */
    @ApiModelProperty(name="code",value = "部门编码")
    private String code;
}
