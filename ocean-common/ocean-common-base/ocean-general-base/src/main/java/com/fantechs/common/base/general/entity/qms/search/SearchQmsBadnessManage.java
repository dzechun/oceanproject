package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsBadnessManage extends BaseQuery implements Serializable {
    /**
     * 来料检验单id
     */
    @ApiModelProperty(name = "incomingInspectionOrderId", value = "来料检验单id")
    private Long incomingInspectionOrderId;

    /**
     * 来料检验单号
     */
    @ApiModelProperty(name = "incomingInspectionOrderCode", value = "来料检验单号")
    private String incomingInspectionOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialCode", value = "产品料号")
    private String materialCode;

    /**
     * 供应商
     */
    @ApiModelProperty(name = "supplierName", value = "供应商")
    private String supplierName;

}
