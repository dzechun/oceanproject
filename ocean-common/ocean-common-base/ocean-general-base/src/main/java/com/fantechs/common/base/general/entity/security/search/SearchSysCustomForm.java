package com.fantechs.common.base.general.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2021/1/8.
 */
@Data
public class SearchSysCustomForm extends BaseQuery implements Serializable {
    @ApiModelProperty(name="customFormCode",value = "所属对象")
    private String customFormCode;

    @ApiModelProperty(name="fromRout",value = "表单路由")
    private String fromRout;
}
