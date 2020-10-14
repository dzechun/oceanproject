package com.fantechs.common.base.entity.apply.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtWorkOrder extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -2565635736485077385L;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
}
