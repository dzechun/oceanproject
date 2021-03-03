package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.List;

@Data
public class SearchWmsOutShippingNote extends BaseQuery implements Serializable {

    /**
     * 出货通知单号
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单号")
    private String shippingNoteCode;

    /**
     * 订单合同号
     */
    @ApiModelProperty(name="orderContractCode",value = "订单合同号")
    private String orderContractCode;

    /**
     * 操作人
     */
    @ApiModelProperty(name="operatorUserName",value = "操作人")
    private String operatorUserName;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    private String processorUserName;

    /**
     * 单据状态（0-待备料 1-备料中 2- 备料完成）
     */
    @ApiModelProperty(name="outStatus",value = "出库状态（0-待出库 1-出库中 2- 出库完成）")
    private Byte outStatus;

    @ApiModelProperty(name="outStatus",value = "备料状态（0-待备料 1-备料中 2- 备料完成）")
    private Byte stockStatus;

    @ApiModelProperty(name="stockStatusIn",value = "备料状态集合")
    private List<Integer> stockStatusIn;

    @ApiModelProperty(name="outStatusIn",value = "出库状态集合")
    private List<Integer> outStatusIn;
}
