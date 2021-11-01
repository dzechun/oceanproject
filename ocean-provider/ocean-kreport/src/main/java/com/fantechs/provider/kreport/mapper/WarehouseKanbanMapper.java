package com.fantechs.provider.kreport.mapper;

import com.fantechs.common.base.general.entity.kreport.*;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseKanbanMapper extends MyMapper<WarehouseKanban> {

    /**
     * 统计作业概况
     */
    WarehouseKanban findOrderQty(Map<String,Object> map);

    /**
     * 当日/月收货统计
     */
    BillsQtyStatistics findClaimGoods(Map<String,Object> map);

    /**
     * 当日/月发货统计
     */
    BillsQtyStatistics findDeliverGoods(Map<String,Object> map);

    /**
     * 当日/月拣货统计
     */
    BillsQtyStatistics findOrderPicking(Map<String,Object> map);

    /**
     * 当日/月补货单
     */
    BillsQtyStatistics findReplenishment(Map<String,Object> map);

    /**
     * 当日/库存未动统计
     */
    InventoryStatistics findInventoriesStill(Map<String,Object> map);

    /**
     * 日发运折线图
     */
    List<QuantityShipments> findDayLineChart(Map<String,Object> map);

    /**
     * 月发运折线图
     */
    List<QuantityShipments> findMonthLineChart(Map<String,Object> map);

    /**
     * 超期拣货单统计
     */
    OverduePicking findOverduePickingt(Map<String,Object> map);

    /**
     * 作业中单据统计
     */
    List<JobDocumentProgress> findJobDocumentProgressList(Map<String,Object> map);

    /**
     * 人员拣货排名
     */
    List<PersonnelPickingRanking> findPersonnelPickingRankingList(Map<String,Object> map);

    /**
     * 获取用户上架数量
     */
    List<PersonnelPickingRanking> findPutawayList(Map<String,Object> map);

    /**
     * 获取用户拣货数量
     */
    List<PersonnelPickingRanking> findPickList(Map<String,Object> map);

    /**
     * 获取用户复核数量
     */
    List<PersonnelPickingRanking> findRecheckList(Map<String,Object> map);

    /**
     * 获取用户补货数量
     */
    List<PersonnelPickingRanking> findReplenishmentList(Map<String,Object> map);

    /**
     * 波次管理
     */
    WaveManager findWaveManager(Map<String,Object> map);

    /**
     * 作业概况
     */
    OperationOverview findOperationOverview(Map<String,Object> map);

    /**
     * 出库折线图
     */
    List<OutboundOverview> findOutboundOverviewList(Map<String,Object> map);

    /**
     * 产品库存占比
     */
    List<InventoryProportion> findInventoryProportionList(Map<String,Object> map);

    List<Warehouse> findWarehouse();
}
