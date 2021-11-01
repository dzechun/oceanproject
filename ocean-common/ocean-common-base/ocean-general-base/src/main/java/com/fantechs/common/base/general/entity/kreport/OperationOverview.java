package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 波次管理
 */
@Data
public class OperationOverview extends ValidGroup implements Serializable {

    /**
     * 打开单量
     */
    @ApiModelProperty(name="openBillsQty",value = "打开单量")
    private BigDecimal openBillsQty;

    /**
     * 生效单量
     */
    @ApiModelProperty(name="takeEffectQty",value = "生效单量")
    private BigDecimal takeEffectQty;

    /**
     * 已分配单量
     */
    @ApiModelProperty(name="allocatedQty",value = "已分配单量")
    private BigDecimal allocatedQty;

    /**
     * 作业中单
     */
    @ApiModelProperty(name="operationQty",value = "作业中单")
    private BigDecimal operationQty;

    /**
     * 作业完成单量
     */
    @ApiModelProperty(name="jobCompleteQty",value = "作业完成单量")
    private BigDecimal jobCompleteQty;

    /**
     * 已发运单量
     */
    @ApiModelProperty(name="shippedQty",value = "已发运单量")
    private BigDecimal shippedQty;

}
