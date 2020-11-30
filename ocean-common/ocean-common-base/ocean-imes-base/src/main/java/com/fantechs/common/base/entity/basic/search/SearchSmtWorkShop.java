package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchSmtWorkShop  extends BaseQuery implements Serializable {


    private static final long serialVersionUID = 5222107074815036921L;
    /**
     * 车间编码
     */
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 车间描述
     */
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    private String workShopDesc;

    /**
     * 厂别id
     */
    @ApiModelProperty(name = "factoryId",value = "厂别id")
    private String factoryId;

}
