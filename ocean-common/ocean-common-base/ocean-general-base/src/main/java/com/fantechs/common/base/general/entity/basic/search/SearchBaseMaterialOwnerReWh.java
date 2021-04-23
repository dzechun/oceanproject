package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangzhongwu
 * @create 2021-04-23 11:53
 */
@Data
public class SearchBaseMaterialOwnerReWh extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 6008776606055814825L;

    /**
     * ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息ID")
    private Long materialOwnerId;
}
