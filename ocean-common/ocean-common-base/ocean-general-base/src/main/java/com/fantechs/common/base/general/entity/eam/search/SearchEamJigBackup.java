package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigBackup extends BaseQuery implements Serializable {

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    private Long jigId;

}
