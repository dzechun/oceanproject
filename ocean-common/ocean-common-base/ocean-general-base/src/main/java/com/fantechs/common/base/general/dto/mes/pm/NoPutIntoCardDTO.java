package com.fantechs.common.base.general.dto.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: bingo.ren
 * @Date: 2021/2/3 15:24
 * @Description: 未开工流程卡
 * @Version: 1.0
 */
@Data
public class NoPutIntoCardDTO {
    @ApiModelProperty(value = "流程卡号",example = "流程卡号")
    private String workOrderCardId;
    @ApiModelProperty(value = "产品名称",example = "产品名称")
    private String MaterialName;
    @ApiModelProperty(value = "部件名称",example = "部件名称")
    private String partsInformationName;
    @ApiModelProperty(value = "流程卡数量",example = "流程卡数量")
    private BigDecimal outPutQty;
    @ApiModelProperty(value = "规格",example = "规格")
    private BigDecimal packageSpecificationQuantity;
    @ApiModelProperty(value = "颜色",example = "颜色")
    private String color;
    @ApiModelProperty(value = "颜色",example = "颜色")
    private Date printDate;
    @ApiModelProperty(value = "工艺路线ID",example = "工艺路线ID")
    private Long routeId;
    @ApiModelProperty(value = "工艺路线工序",example = "工艺路线工序")
    private String routeProcess;
}
