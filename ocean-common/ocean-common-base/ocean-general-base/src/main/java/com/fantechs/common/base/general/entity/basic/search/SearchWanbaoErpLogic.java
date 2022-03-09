package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2022/3/3
 */
@Data
public class SearchWanbaoErpLogic extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "logicCode",value = "erp逻辑仓编码")
    private String logicCode;

    @ApiModelProperty(name = "logicName",value = "erp逻辑仓名称")
    private String logicName;
}
