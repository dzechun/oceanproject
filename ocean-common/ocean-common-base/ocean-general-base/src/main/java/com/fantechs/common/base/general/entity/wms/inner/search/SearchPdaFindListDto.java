package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPdaFindListDto extends BaseQuery implements Serializable {

    @ApiModelProperty(name="jobOrderCode",value = "作业单号，非必填")
    private String jobOrderCode;
}
