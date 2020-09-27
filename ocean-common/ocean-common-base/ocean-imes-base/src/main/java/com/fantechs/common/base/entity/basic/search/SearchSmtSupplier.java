package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/9/27
 */
@ApiModel
@Data
public class SearchSmtSupplier extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -2927770419793897574L;
    @ApiModelProperty("供应商代码")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("供应商描述")
    private String supplierDesc;
}
