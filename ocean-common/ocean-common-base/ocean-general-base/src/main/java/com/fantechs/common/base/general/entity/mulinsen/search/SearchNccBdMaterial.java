package com.fantechs.common.base.general.entity.mulinsen.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchNccBdMaterial extends BaseQuery implements Serializable {

    @ApiModelProperty(name="materialtype",value = "规格型号")
    private String materialtype;
}
