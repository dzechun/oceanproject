package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/10 18:19
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseProductModel extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -4596032997885676890L;

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    private String productModelCode;

    /**
     *  产品型号描述
     */
    @ApiModelProperty(name="productModelDesc" ,value="产品型号描述")
    private String productModelDesc;

    /**
     *  产品型号名称
     */
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    private String productModelName;

    /**
     *  产品族名称
     */
    @ApiModelProperty(name="productFamilyName" ,value="产品族名称")
    private String productFamilyName;

}
