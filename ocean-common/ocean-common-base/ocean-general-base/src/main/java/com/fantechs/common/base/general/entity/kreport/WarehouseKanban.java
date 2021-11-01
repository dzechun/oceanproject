package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WarehouseKanban extends ValidGroup implements Serializable {

    @Id
    private Long id;

    /**
     * 已发运订单
     */
    @ApiModelProperty(name="finishedOrderQty",value = "已发运订单")
    private BigDecimal finishedOrderQty;

    /**
     * 未发运订单
     */
    @ApiModelProperty(name="unfinishedOrderQty",value = "未发运订单")
    private BigDecimal unfinishedOrderQty;

    /**
     * 待完成订单
     */
    @ApiModelProperty(name="pendingSalesQty",value = "待完成订单")
    private BigDecimal pendingSalesQty;

    /**
     * 收货统计
     */
    @ApiModelProperty(name="claimGoods",value = "收货统计")
    private BillsQtyStatistics claimGoods;

    /**
     * 发货统计
     */
    @ApiModelProperty(name="deliverGoods",value = "发货统计")
    private BillsQtyStatistics deliverGoods;

    /**
     * 拣货统计
     */
    @ApiModelProperty(name="orderPicking",value = "拣货统计")
    private BillsQtyStatistics orderPicking;

    /**
     * 波次单统计
     */
    @ApiModelProperty(name="wavePicking",value = "波次单统计")
    private BillsQtyStatistics wavePicking;

    /**
     * 补货统计
     */
    @ApiModelProperty(name="replenishment",value = "补货统计")
    private BillsQtyStatistics replenishment;

    /**
     * 移位单统计
     */
    @ApiModelProperty(name="shifting",value = "移位单统计")
    private BillsQtyStatistics shifting;

    /**
     * 库存未动统计
     */
    @ApiModelProperty(name="inventoriesStill",value = "库存未动统计")
    private InventoryStatistics inventoriesStill;

    /**
     * 库存爆款统计（前三）
     */
    @ApiModelProperty(name="inventoriesFaddish",value = "库存爆款统计（前三）")
    private List<InventoryStatistics> inventoriesFaddishs;

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
     * 超期拣货单统计
     */
    @ApiModelProperty(name="overduePicking",value = "超期拣货单统计")
    private OverduePicking overduePicking;

    /**
     * 作业中单据统计
     */
    @ApiModelProperty(name="jobDocumentProgress",value = "作业中单据统计")
    private List<JobDocumentProgress> jobDocumentProgressList;

    /**
     * 人员拣货排名
     */
    @ApiModelProperty(name="personnelPickingRankingList",value = "人员拣货排名")
    private List<PersonnelPickingRanking> personnelPickingRankingList;

    /**
     * 波次管理
     */
    @ApiModelProperty(name="waveManager",value = "波次管理")
    private WaveManager waveManager;

    /**
     * 作业概况
     */
    @ApiModelProperty(name="OperationOverview",value = "作业概况")
    private OperationOverview operationOverview;

    /**
     * 出库概况折线图
     */
    @ApiModelProperty(name="outboundOverviewList",value = "出库概况折线图")
    private List<OutboundOverview> outboundOverviewList;

    /**
     * 产品库存占比
     */
    @ApiModelProperty(name="inventoryProportionList",value = "产品库存占比")
    private List<InventoryProportion> inventoryProportionList;


}
