package com.fantechs.common.base.general.entity.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEsopWiReleaseDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="wiReleaseId",value = "ESOP发布管理ID")
    private Long wiReleaseId;
}
