package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsOutDeliveryOrder extends BaseQuery implements Serializable {

    /**
     * 出库单编码
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出库单号")
    private String deliveryOrderCode;

    /**
     * 出货通知单
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单")
    private String shippingNoteCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    private String processorUserName;


}
