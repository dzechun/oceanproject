package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/11/21
 */
@Data
public class SearchSmtWorkOrderBarcodeCollocation extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 9040516370290167214L;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;
}
