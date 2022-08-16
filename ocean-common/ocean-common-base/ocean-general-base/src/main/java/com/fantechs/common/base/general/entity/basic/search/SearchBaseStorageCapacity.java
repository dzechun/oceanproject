package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBaseStorageCapacity extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "materialId",value = "物料id")
    private Long materialId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;

    /**
     * 产品存储类型(1-A类 2-B类 3-C类 4-D类)
     */
    @ApiModelProperty(name = "materialStoreType",value = "产品存储类型(1-A类 2-B类 3-C类 4-D类)")
    private Integer materialStoreType;

    /**
     * 物料编码前缀
     */
    @ApiModelProperty(name="materialCodePrefix",value = "物料编码前缀")
    private String materialCodePrefix;

}
