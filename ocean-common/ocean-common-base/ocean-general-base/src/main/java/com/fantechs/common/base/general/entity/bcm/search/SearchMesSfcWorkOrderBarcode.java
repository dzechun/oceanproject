package com.fantechs.common.base.general.entity.bcm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/4/7
 */
@Data
public class SearchMesSfcWorkOrderBarcode extends BaseQuery implements Serializable {
    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 条码类别（1.工序流转卡、2.工单条码、3.客户条码、4-销售订单条码）
     */
    @ApiModelProperty(name="barcodeType",value = "条码类别（1.工序流转卡、2.工单条码、3.客户条码、4-销售订单条码）")
    private Byte barcodeType;
}
