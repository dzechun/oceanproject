package com.fantechs.common.base.general.dto.wms.in.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsInInPlanOrderImport implements Serializable {

    /**
     * 入库计划标识
     */
    @ApiModelProperty(name="id",value = "入库计划标识")
    @Excel(name = "送货计划标识", height = 20, width = 30)
    private String id;

    /**
     *
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30)
    private String warehouseName;

    /**
     * 计划开始时间
     */
    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    @Excel(name = "计划开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    /**
     * 计划完成时间
     */
    @ApiModelProperty(name="planEndTime",value = "计划完成时间")
    @Excel(name = "计划完成时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30)
    private BigDecimal planQty;

    /**
     * 上架数量
     */
    @ApiModelProperty(name="putawayQty",value = "上架数量")
    @Excel(name = "上架数量", height = 20, width = 30)
    private BigDecimal putawayQty;

    /**
     * 生产日期(生产时间)
     */
    @ApiModelProperty(name="productionTime",value = "生产日期(生产时间)")
    @Excel(name = "生产日期(生产时间)", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date productionTime;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30)
    private String batchCode;

}
