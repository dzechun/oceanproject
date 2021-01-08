package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchBaseTab extends BaseQuery implements Serializable {

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId",value = "物料ID")
    private Long materialId;

}
