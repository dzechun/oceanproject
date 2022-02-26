package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 10:10
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseOrderFlowSourceDet extends BaseQuery implements Serializable {

    /**
     * 来源单据类型编码
     */
    @ApiModelProperty(name="sourceOrderTypeCode",value = "来源单据类型编码")
    private String sourceOrderTypeCode;

    /**
     * 下推单据类型编码
     */
    @ApiModelProperty(name="nextOrderTypeCode",value = "下推单据类型编码")
    private String nextOrderTypeCode;

}
