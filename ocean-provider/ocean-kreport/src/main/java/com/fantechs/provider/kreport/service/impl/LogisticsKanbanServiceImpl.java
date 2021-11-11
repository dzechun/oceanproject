package com.fantechs.provider.kreport.service.impl;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.general.entity.kreport.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.AddressUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.kreport.mapper.LogisticsKanbanMapper;
import com.fantechs.provider.kreport.service.LogisticsKanbanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class LogisticsKanbanServiceImpl extends BaseService<LogisticsKanban> implements LogisticsKanbanService {

    @Resource
    private LogisticsKanbanMapper logisticsKanbanMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public LogisticsKanban findKanbanData(Map<String, Object> map) {
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("carrierNames");
        List<SysSpecItem> carrierNames = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (StringUtils.isNotEmpty(carrierNames)){
            List<String> carrierNameList = Arrays.asList(carrierNames.get(0).getParaValue().split(","));
            map.put("carrierNames",carrierNameList);
        }
        searchSysSpecItem.setSpecCode("billTypeNames");
        List<SysSpecItem> billTypeNames = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (StringUtils.isNotEmpty(billTypeNames)){
            List<String> billTypeNameList = Arrays.asList(billTypeNames.get(0).getParaValue().split(","));
            map.put("billTypeNames",billTypeNameList);
        }
        int startPage = Integer.valueOf(map.get("startPage").toString()).intValue();
        int pageSize = Integer.valueOf(map.get("pageSize").toString());
        startPage = (startPage-1) * pageSize;
        map.put("startPage",startPage);

        LogisticsKanban logisticsKanban = logisticsKanbanMapper.findMaterialTrajectory(map);
        List<QuantityShipments> dayLineChart = logisticsKanbanMapper.findDayLineChart(map);
        List<QuantityShipments> monthLineChart = logisticsKanbanMapper.findMonthLineChart(map);
        map.put("isDay",1);
//        List<CarrierProcessingOrder> dayCarrierProcessingOrderList = logisticsKanbanMapper.findCarrierProcessingOrderList(map);
        map.put("isDay",2);
//        List<CarrierProcessingOrder> monthCarrierProcessingOrderList = logisticsKanbanMapper.findCarrierProcessingOrderList(map);
        List<RadiationChart> radiationChartList = logisticsKanbanMapper.findRadiationChartList(map);
        if (StringUtils.isNotEmpty(radiationChartList)) {
            Object lngAndLat = redisUtil.get(radiationChartList.get(0).getWarehouseDistrictName());
            if (StringUtils.isNotEmpty(lngAndLat) && !lngAndLat.toString().contains("<")) {
                String[] split = lngAndLat.toString().split(",");
                RadiationChart radiationChart = new RadiationChart();
                radiationChart.setDistrictName(radiationChartList.get(0).getWarehouseDistrictName());
                radiationChart.setLng(split[0]);
                radiationChart.setLat(split[1]);
                logisticsKanban.setRadiationChart(radiationChart);
            }else {
                try {
                    RadiationChart radiationChart = new RadiationChart();
                    String[] coordinate = AddressUtils.getCoordinate(radiationChartList.get(0).getWarehouseDistrictName());
                    radiationChart.setDistrictName(radiationChartList.get(0).getWarehouseDistrictName());
                    radiationChart.setWarehouseDistrictName(radiationChartList.get(0).getWarehouseDistrictName());
                    radiationChart.setLng(coordinate[0]);
                    radiationChart.setLat(coordinate[1]);
                    redisUtil.set("address:"+radiationChart.getDistrictName(),coordinate[0]+","+coordinate[1],86400);
                    logisticsKanban.setRadiationChart(radiationChart);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Object address = redisUtil.get("address");
            List<RadiationChart> radiationCharts = null;
            if (StringUtils.isNotEmpty(address)) {
                radiationCharts = JsonUtils.jsonToList(address.toString(), RadiationChart.class);
            }
            boolean isUpdate = false;
            for (RadiationChart radiationChart : radiationChartList) {
                boolean b = false;
                if (radiationChart.getDistrictName().contains("-")) {
                    continue;
                }
                if (StringUtils.isNotEmpty(radiationCharts)) {
                    for (RadiationChart chart : radiationCharts) {
                        if (chart.getDistrictName().equals(radiationChart.getDistrictName()) && StringUtils.isNotEmpty(chart.getLng(),chart.getLat())) {
                            radiationChart.setLng(chart.getLng());
                            radiationChart.setLat(chart.getLat());

                            b = true;
                        }
                    }
                }
                if (b) {
                    continue;
                }
                isUpdate = true;
                try {
                    String[] coordinate = AddressUtils.getCoordinate(radiationChart.getDistrictName());
                    if (coordinate[0].contains("<")) {
                        continue;
                    }
                    radiationChart.setLng(coordinate[0]);
                    radiationChart.setLat(coordinate[1]);
//                    redisUtil.set("address:"+radiationChart.getDistrictName(),coordinate[0]+","+coordinate[1],86400);
                } catch (Exception e) {
                    continue;
                }
            }
            if (isUpdate) {
                redisUtil.set("address", JsonUtils.objectToJson(radiationChartList),24*60*60);
            }
        }
//        List<TransportInformation> transportInformationList = logisticsKanbanMapper.findTransportInformationList(map);

        logisticsKanban.setRadiationChartList(radiationChartList);
//        logisticsKanban.setTransportInformationList(transportInformationList);
//        logisticsKanban.setDayCarrierProcessingOrderList(dayCarrierProcessingOrderList);
//        logisticsKanban.setMonthCarrierProcessingOrderList(monthCarrierProcessingOrderList);
        logisticsKanban.setMonthLineChart(monthLineChart);
        logisticsKanban.setDayLineChart(dayLineChart);
        return logisticsKanban;
    }


    @Override
    public List<TransportInformation> findTransportInformationList(Map<String, Object> map) {
        int startPage = Integer.valueOf(map.get("startPage").toString()).intValue();
        int pageSize = Integer.valueOf(map.get("pageSize").toString());
        startPage = (startPage-1) * pageSize;
        map.put("startPage",startPage);
        return logisticsKanbanMapper.findTransportInformationList(map);
    }

    @Override
    public List<CarrierProcessingOrder> findCarrierProcessingOrderList(Map<String, Object> map) {
        return logisticsKanbanMapper.findCarrierProcessingOrderList(map);
    }
}
