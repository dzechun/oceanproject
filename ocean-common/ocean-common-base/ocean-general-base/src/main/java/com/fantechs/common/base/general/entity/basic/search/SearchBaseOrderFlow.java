package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseOrderFlow extends BaseQuery implements Serializable {

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
     * 单据节点(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)
     */
    @ApiModelProperty(name="orderNode",value = "单据节点(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)")
    private Byte orderNode;

    /**
     * 来源单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)
     */
    @ApiModelProperty(name="sourceOrder",value = "来源单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)")
    private Byte sourceOrder;

    /**
     * 下推单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)
     */
    @ApiModelProperty(name="pushDownOrder",value = "下推单据(1-订单模块 2-收货计划 3-收货作业 4-来料检验 5-入库计划 6-上架作业)")
    private Byte pushDownOrder;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;
}
