package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
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
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private String workOrderId;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId",value = "产品料号ID")
    private String materialId;

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

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private String processId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;


    private static final long serialVersionUID = 1L;
}
