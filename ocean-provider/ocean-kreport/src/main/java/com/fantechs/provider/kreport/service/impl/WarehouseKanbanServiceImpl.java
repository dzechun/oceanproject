package com.fantechs.provider.kreport.service.impl;

import com.fantechs.common.base.general.entity.kreport.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.kreport.mapper.WarehouseKanbanMapper;
import com.fantechs.provider.kreport.service.WarehouseKanbanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarehouseKanbanServiceImpl extends BaseService<WarehouseKanban> implements WarehouseKanbanService {

    @Resource
    private WarehouseKanbanMapper warehouseKanbanMapper;
    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public WarehouseKanban findKanbanData(Map<String, Object> map) {

//        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
//        searchSysSpecItem.setSpecCode("codes");
//        List<SysSpecItem> carrierNames = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
//        if (StringUtils.isNotEmpty(carrierNames)){
//            List<String> carrierNameList = Arrays.asList(carrierNames.get(0).getParaValue().split(","));
//            map.put("codes",carrierNameList);
//        }

        WarehouseKanban orderQty = warehouseKanbanMapper.findOrderQty(map);
        BillsQtyStatistics claimGoods = warehouseKanbanMapper.findClaimGoods(map);
        BillsQtyStatistics deliverGoods = warehouseKanbanMapper.findDeliverGoods(map);
        BillsQtyStatistics orderPicking = warehouseKanbanMapper.findOrderPicking(map);
        BillsQtyStatistics replenishment = warehouseKanbanMapper.findReplenishment(map);
        InventoryStatistics inventoriesStill = warehouseKanbanMapper.findInventoriesStill(map);
        List<QuantityShipments> dayLineChart = warehouseKanbanMapper.findDayLineChart(map);
        List<QuantityShipments> monthLineChart = warehouseKanbanMapper.findMonthLineChart(map);
        OverduePicking overduePickingt = warehouseKanbanMapper.findOverduePickingt(map);
//        List<JobDocumentProgress> jobDocumentProgressList = warehouseKanbanMapper.findJobDocumentProgressList(map);
//        List<PersonnelPickingRanking> personnelPickingRankingList = warehouseKanbanMapper.findPersonnelPickingRankingList(map);
        WaveManager waveManager = warehouseKanbanMapper.findWaveManager(map);
        OperationOverview operationOverview = warehouseKanbanMapper.findOperationOverview(map);
        List<OutboundOverview> outboundOverviewList = warehouseKanbanMapper.findOutboundOverviewList(map);
        List<InventoryProportion> inventoryProportionList = warehouseKanbanMapper.findInventoryProportionList(map);

        orderQty.setClaimGoods(claimGoods);
        orderQty.setDeliverGoods(deliverGoods);
        orderQty.setOrderPicking(orderPicking);
        orderQty.setReplenishment(replenishment);
        orderQty.setInventoriesStill(inventoriesStill);
        orderQty.setDayLineChart(dayLineChart);
        orderQty.setMonthLineChart(monthLineChart);
        orderQty.setOverduePicking(overduePickingt);
//        orderQty.setJobDocumentProgressList(jobDocumentProgressList);
//        orderQty.setPersonnelPickingRankingList(personnelPickingRankingList);
        orderQty.setWaveManager(waveManager);
        orderQty.setOperationOverview(operationOverview);
        orderQty.setOutboundOverviewList(outboundOverviewList);
        orderQty.setInventoryProportionList(inventoryProportionList);
        return orderQty;
    }

    @Override
    public List<Warehouse> findWarehouse() {
        return warehouseKanbanMapper.findWarehouse();
    }

    @Override
    public List<PersonnelPickingRanking> findPersonnelPickingRankingList(Map<String, Object> map) {
        List<PersonnelPickingRanking> personnelPickingRankingList = warehouseKanbanMapper.findPersonnelPickingRankingList(map);
        List<PersonnelPickingRanking> putawayList = warehouseKanbanMapper.findPutawayList(map);
        List<PersonnelPickingRanking> pickList = warehouseKanbanMapper.findPickList(map);
        List<PersonnelPickingRanking> recheckList = warehouseKanbanMapper.findRecheckList(map);
        List<PersonnelPickingRanking> replenishmentList = warehouseKanbanMapper.findReplenishmentList(map);

        for (PersonnelPickingRanking personnelPickingRanking : personnelPickingRankingList) {
            BigDecimal qty = new BigDecimal(0);
            for (PersonnelPickingRanking pickingRanking : putawayList) {
                if (personnelPickingRanking.getUnikey().equals(pickingRanking.getUnikey())){
                    personnelPickingRanking.setPutawayQty(pickingRanking.getPutawayQty());
                    qty = qty.add(pickingRanking.getPutawayQty());
                    break;
                }
            }
            for (PersonnelPickingRanking pickingRanking : pickList) {
                if (personnelPickingRanking.getUnikey().equals(pickingRanking.getUnikey())){
                    personnelPickingRanking.setPickQty(pickingRanking.getPickQty());
                    qty = qty.add(pickingRanking.getPickQty());
                    break;
                }
            }
            for (PersonnelPickingRanking pickingRanking : recheckList) {
                if (personnelPickingRanking.getUnikey().equals(pickingRanking.getUnikey())){
                    personnelPickingRanking.setRecheckQty(pickingRanking.getRecheckQty());
                    qty = qty.add(pickingRanking.getRecheckQty());
                    break;
                }
            }
            for (PersonnelPickingRanking pickingRanking : replenishmentList) {
                if (personnelPickingRanking.getUnikey().equals(pickingRanking.getUnikey())){
                    personnelPickingRanking.setReplenishmentQty(pickingRanking.getReplenishmentQty());
                    qty = qty.add(pickingRanking.getReplenishmentQty());
                    break;
                }
            }
            personnelPickingRanking.setQty(qty);
        }
        personnelPickingRankingList = personnelPickingRankingList.stream()
                .sorted(Comparator.comparing(PersonnelPickingRanking::getQty).reversed()).collect(Collectors.toList());
        return  personnelPickingRankingList;
    }


    @Override
    public List<JobDocumentProgress> findJobDocumentProgressList(Map<String, Object> map) {
        return warehouseKanbanMapper.findJobDocumentProgressList(map);
    }
}
