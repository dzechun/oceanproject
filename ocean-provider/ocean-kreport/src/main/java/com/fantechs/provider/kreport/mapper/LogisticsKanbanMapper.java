package com.fantechs.provider.kreport.mapper;

import com.fantechs.common.base.general.entity.kreport.*;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogisticsKanbanMapper extends MyMapper<LogisticsKanban> {

    /**
     * 日发运折线图
     */
    List<QuantityShipments> findDayLineChart(Map<String,Object> map);

    /**
     * 月发运折线图
     */
    List<QuantityShipments> findMonthLineChart(Map<String,Object> map);

    /**
     * 统计物流轨迹信息
     */
    LogisticsKanban findMaterialTrajectory(Map<String,Object> map);

    /**
     * 承运商发运量
     */
    List<CarrierProcessingOrder> findCarrierProcessingOrderList(Map<String,Object> map);

    /**
     * 其他地址信息
     */
    List<RadiationChart> findRadiationChartList(Map<String,Object> map);

    /**
     * 运输地信息
     */
    List<TransportInformation> findTransportInformationList(Map<String,Object> map);
}
