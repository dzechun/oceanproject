package com.fantechs.common.base.general.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2021/1/8.
 */
@Data
public class SearchSysCustomFormDet extends BaseQuery implements Serializable {
    @ApiModelProperty(name="customFormId",value = "自定义表单Id")
    private Long customFormId;
}
