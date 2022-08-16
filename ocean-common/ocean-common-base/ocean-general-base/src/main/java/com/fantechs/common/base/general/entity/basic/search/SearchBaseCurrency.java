package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchBaseCurrency extends BaseQuery implements Serializable {

    /**
     * 币别编码
     */
    @ApiModelProperty(name="currencyCode",value = "币别编码")
    private String currencyCode;

    /**
     * 币别名称
     */
    @ApiModelProperty(name="currencyName",value = "币别名称")
    private String currencyName;
}
