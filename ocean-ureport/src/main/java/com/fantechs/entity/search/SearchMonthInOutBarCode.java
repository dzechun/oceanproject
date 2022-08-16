package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/11/24
 */
@Data
public class SearchMonthInOutBarCode extends BaseQuery implements Serializable {

    /**
     * 业务员
     */
    @ApiModelProperty(name = "salesUserName",value = "业务员")
    private String salesUserName;

    /**
     * 客户id
     */
    @ApiModelProperty(name = "supplierId",value = "客户")
    private String supplierId;

    /**
     * 产品id
     */
    @ApiModelProperty(name = "materialId",value = "产品id")
    private String materialId;
}
