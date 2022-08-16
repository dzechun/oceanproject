package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/9/6
 */
@Data
public class MesSfcRepairOrderPrintParam implements Serializable {

    @ApiModelProperty(name = "printName",value = "打印机名称")
    private String printName;

    @ApiModelProperty(name = "size",value = "打印数量")
    private Integer size;

    @ApiModelProperty(name = "labelName",value = "标签模版")
    private String labelName;

    @ApiModelProperty(name = "repairOrderId",value = "维修单id")
    private Long repairOrderId;
}
