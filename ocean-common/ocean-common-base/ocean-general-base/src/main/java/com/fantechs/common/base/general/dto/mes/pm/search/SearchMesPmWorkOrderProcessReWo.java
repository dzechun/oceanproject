package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchMesPmWorkOrderProcessReWo extends BaseQuery implements Serializable {

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 产品料号描述
     */
    @ApiModelProperty(name="materialDesc",value = "产品料号描述")
    private String materialDesc;


    private static final long serialVersionUID = 1L;
}
