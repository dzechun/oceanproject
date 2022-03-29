package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/9/24
 */
@Data
public class SearchOmInStorage extends BaseQuery implements Serializable {

    private String supplierName;

    private String salesOrderCode;

    private String materialName;

    private String materialCode;

    private String packingUnitName;

    private String warehouseName;
}
