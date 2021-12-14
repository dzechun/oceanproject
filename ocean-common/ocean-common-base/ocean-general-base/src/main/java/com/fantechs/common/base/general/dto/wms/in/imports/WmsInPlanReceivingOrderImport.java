package com.fantechs.common.base.general.dto.wms.in.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/12/13
 */
@Data
public class WmsInPlanReceivingOrderImport implements Serializable {

    /**
     *
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30)
    private String warehouseName;

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
