package com.fantechs.entity.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
@Data
public class SearchMonthInOut extends BaseQuery implements Serializable {
    /**
     * 业务员
     */
    @ApiModelProperty(name = "salesUserName",value = "业务员")
    private String salesUserName;

    /**
     * 客户
     */
    @ApiModelProperty(name = "supplierName",value = "客户")
    private String supplierName;

    /**
     * 型号
     */
    @ApiModelProperty(name = "productModelCode",value = "型号")
    private String productModelCode;

    /**
     * 产品编码
     */
    @ApiModelProperty(name = "materialCode",value = "产品编码")
    private String materialCode;
}
