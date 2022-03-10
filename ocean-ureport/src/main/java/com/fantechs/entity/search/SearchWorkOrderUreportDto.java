package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWorkOrderUreportDto extends BaseQuery implements Serializable {


    @ApiModelProperty(name="proLineName",value = "产线名称")
    private String proLineName;

    @ApiModelProperty(name="productModelCode",value = "客户型号")
    private String productModelCode;

    @ApiModelProperty(name = "materialCode",value = "物料编码/万宝型号")
    private String materialCode;

    @ApiModelProperty(name="workOrderCode",value = "工单号/计划名字")
    private String workOrderCode;

    @ApiModelProperty(name="work_order_status" ,value="订单状态/计划状态")
    private String workOrderStatus;

    @ApiModelProperty(name="type",value = "计划类型")
    private String type;
}
