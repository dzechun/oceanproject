package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SaveMesPmMatchingOrderDto implements Serializable {

    /**
     * 配套单ID
     */
    @ApiModelProperty(name="matchingOrderId",value = "配套单ID")
    private Long matchingOrderId;

    /**
     * 配套单号
     */
    @ApiModelProperty(name="matchingOrderCode",value = "配套单号")
    private String matchingOrderCode;

    /**
     * 流程单ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "流程单ID")
    private Long workOrderCardPoolId;

    /**
     * 员工ID
     */
    @ApiModelProperty(name="staffId",value = "员工ID")
    private Long staffId;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQuantity",value = "工单数量")
    private Integer workOrderQuantity;

    /**
     * 生产数量
     */
    @ApiModelProperty(name="productionQuantity",value = "生产数量")
    private Integer productionQuantity;

    /**
     * 配套数量
     */
    @ApiModelProperty(name="matchingQuantity",value = "配套数量")
    private BigDecimal matchingQuantity;

    /**
     * 最小齐套数
     */
    @ApiModelProperty(name="minMatchingQuantity",value = "最小齐套数")
    private BigDecimal minMatchingQuantity;

    /**
     * 状态(0.待配套 ，1.配套中 2.配套完成)
     */
    @ApiModelProperty(name="status",value = "状态(0.待配套 ，1.配套中 2.配套完成)")
    private Byte status;


    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Transient
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Transient
    private Long materialId;
}
