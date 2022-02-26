package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 10:10
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseOrderFlowSource extends BaseQuery implements Serializable {

    /**
     * 作业模块(1-入库作业 2-库内作业 3-出库作业)
     */
    @ApiModelProperty(name="operationModule",value = "作业模块(1-入库作业 2-库内作业 3-出库作业)")
    private Byte operationModule;

    /**
     * 业务类型(1-采购入库 2-完工入库 3-销退入库 4-其它入库)
     */
    @ApiModelProperty(name="businessType",value = "业务类型(1-采购入库 2-完工入库 3-销退入库 4-其它入库)")
    private Byte businessType;

    /**
     * 单据流维度(1-通用 2-供应商 3-物料)
     */
    @ApiModelProperty(name="orderFlowDimension",value = "单据流维度(1-通用 2-供应商 3-物料)")
    private Byte orderFlowDimension;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    private String orderTypeCode;

}
