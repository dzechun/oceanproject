package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanStockList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesPmDailyPlanStockListDto extends MesPmDailyPlanStockList implements Serializable {

    /**
     * 生产日计划单号
     */
    @Transient
    @ApiModelProperty(name="dailyPlanCode",value = "生产日计划单号")
    private String dailyPlanCode;


    /**
     * 生产订单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "生产订单号")
    private String workOrderCode;

    /**
     * 生产订单ID
     */
    @Transient
    @ApiModelProperty(name="workOrderId",value = "生产订单ID")
    private Long workOrderId;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="salesOrderCode",value = "订单号")
    private String salesOrderCode;

    /**
     * 核心单据编码
     */
    @Transient
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心单据编码")
    private String coreSourceSysOrderTypeCode;

    /**
     * 产品编码.
     */
    @Transient
    @ApiModelProperty(name="materialCodeProduct" ,value="产品编码")
    private String materialCodeProduct;


    /**
     * 产品名称.
     */
    @Transient
    @ApiModelProperty(name="materialNameProduct" ,value="产品名称")
    private String materialNameProduct;

    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;


    /**
     * 物料名称.
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;

    /**
     * 下发数量
     */
    @Transient
    @ApiModelProperty(name="issueQty",value = "下发数量")
    private BigDecimal issueQty;

    /**
     * 仓库ID
     */
    @Transient
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

}
