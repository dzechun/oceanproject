package com.fantechs.common.base.general.dto.eng;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/9/6
 */
@Data
public class EngPackingOrderPrintParam implements Serializable {
    @ApiModelProperty(name = "ids",value = "包装箱号打印(packingOrderSummaryId),内箱码打印/材料编码打印(packingOrderSummaryDetId)")
    private List<Long> ids;

    @ApiModelProperty(name = "type",value = "1-包装箱号打印，2-内箱码打印，3-材料编码打印")
    private Byte type;

    @ApiModelProperty(name = "printName",value = "打印机名称")
    private String printName;

    @ApiModelProperty(name = "size",value = "打印数量")
    private Integer size;

    @ApiModelProperty(name = "printMode",value = "模版名称")
    private String printMode;
}
