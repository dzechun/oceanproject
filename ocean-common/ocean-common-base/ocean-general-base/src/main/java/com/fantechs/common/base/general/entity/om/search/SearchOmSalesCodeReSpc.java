package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchOmSalesCodeReSpc extends BaseQuery implements Serializable {

    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    @ApiModelProperty(name="samePackageCode",value = "PO号")
    private String samePackageCode;

    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    @ApiModelProperty(name="samePackageCodeStatus",value = "同包装编码状态(1:激活;2:失效;3：关闭；)")
    private Byte samePackageCodeStatus;
}
