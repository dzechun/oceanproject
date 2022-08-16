package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/28
 */
@Data
public class StorageRuleDto implements Serializable {
    /**
     * 库位ID
     */
    @ApiModelProperty(name = "storageId",value = "库位ID")
    private Long storageId;

    /**
     * 巷道
     */
    @ApiModelProperty(name="roadway",value = "巷道")
    private Integer roadway;

    /**
     * 排
     */
    @ApiModelProperty(name="rowNo",value = "排")
    private Integer rowNo;

    /**
     * 列
     */
    @ApiModelProperty(name="columnNo",value = "列")
    private Integer columnNo;

    /**
     * 层
     */
    @ApiModelProperty(name="levelNo",value = "层")
    private Integer levelNo;

    /**
     * 上架动线号
     */
    @ApiModelProperty(name="putawayMoveLineNo",value = "上架动线号")
    private Integer putawayMoveLineNo;

    /**
     * 剩余载重
     */
    @ApiModelProperty(name="surplusLoad",value = "剩余载重")
    private BigDecimal surplusLoad;

    /**
     * 剩余容积
     */
    @ApiModelProperty(name="surplusVolume",value = "剩余容积")
    private BigDecimal surplusVolume;

    /**
     * 剩余可放托盘数
     */
    @ApiModelProperty(name="surplusCanPutSalver",value = "剩余可放托盘数")
    private Integer surplusCanPutSalver;

    /**
     * 物料体积
     */
    private BigDecimal volume;

    /**
     * 物料净重
     */
    private BigDecimal netWeight;

    //=======================================

    /**
     * 按体积可上架数
     */
    private BigDecimal volumeQty;
    /**
     * 按重量可上架数
     */
    private BigDecimal netWeightQty;
    /**
     * 可上架数
     */
    private BigDecimal putawayQty;
}
