package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInOtherin extends BaseQuery implements Serializable {

    /**
     * 其他入库单号
     */
    @ApiModelProperty(name="otherinCode",value = "其他入库单号")
    private String otherinCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 处理人名称
     */
    @ApiModelProperty(name="operatorUserName",value = "处理人")
    private String operatorUserName;

}
