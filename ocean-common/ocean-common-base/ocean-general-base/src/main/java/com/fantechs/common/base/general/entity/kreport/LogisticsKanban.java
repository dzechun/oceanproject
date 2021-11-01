package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LogisticsKanban extends ValidGroup implements Serializable {

    @Id
    private Long id;

    /**
     * 日发运折线图
     */
    @ApiModelProperty(name="dayLineChart",value = "日发运折线图")
    private List<QuantityShipments> dayLineChart;;

    /**
     * 月发运折线图
     */
    @ApiModelProperty(name="monthLineChart",value = "月发运折线图")
    private List<QuantityShipments> monthLineChart;

    /**
     * 待发
     */
    @ApiModelProperty(name="dueOut",value = "待发")
    private BigDecimal dueOut;

    /**
     * 在途
     */
    @ApiModelProperty(name="onPassage",value = "在途")
    private BigDecimal onPassage;

    /**
     * 签收
     */
    @ApiModelProperty(name="signFor",value = "签收")
    private BigDecimal signFor;

    /**
     * 总量
     */
    @ApiModelProperty(name="total",value = "总量")
    private BigDecimal total;


    /**
     * 今日承运商发运量
     */
    @ApiModelProperty(name="dayCarrierProcessingOrderList",value = "今日承运商发运量")
    private List<CarrierProcessingOrder> dayCarrierProcessingOrderList;

    /**
     * 当月承运商发运量
     */
    @ApiModelProperty(name="monthCarrierProcessingOrderList",value = "月承运商发运量")
    private List<CarrierProcessingOrder> monthCarrierProcessingOrderList;

    /**
     * 仓库地址信息
     */
    @ApiModelProperty(name="radiationChart",value = "仓库地址信息")
    private RadiationChart radiationChart;

    /**
     * 其他地址信息
     */
    @ApiModelProperty(name="radiationChartList",value = "其他地址信息")
    private List<RadiationChart> radiationChartList;

    /**
     * 运输地信息
     */
    @ApiModelProperty(name="transportInformationList",value = "运输地信息")
    private List<TransportInformation> transportInformationList;


}
