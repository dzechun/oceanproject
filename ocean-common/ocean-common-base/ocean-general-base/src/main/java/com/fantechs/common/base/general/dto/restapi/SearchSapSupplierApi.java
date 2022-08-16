package com.fantechs.common.base.general.dto.restapi;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapSupplierApi implements Serializable {

    /**
     * 工厂
     */
    @ApiModelProperty(name="werks" ,value="工厂")
    private String werks;

}
