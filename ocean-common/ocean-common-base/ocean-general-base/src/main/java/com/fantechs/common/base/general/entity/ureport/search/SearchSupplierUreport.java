package com.fantechs.common.base.general.entity.ureport.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class SearchSupplierUreport extends BaseQuery implements Serializable {
    @ApiModelProperty("供应商代码")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;

}
