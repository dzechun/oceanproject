package com.fantechs.common.base.general.dto.wms.out;

import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutPlanStockListOrderDetDto extends WmsOutPlanStockListOrderDet implements Serializable {

    /**
     * 生产日计划单号
     */
    @Transient
    @ApiModelProperty(name="dailyPlanCode",value = "生产日计划单号")
    private String dailyPlanCode;

    /**
     * 备料计划单号
     */
    @Transient
    @ApiModelProperty(name="planStockListOrderCode",value = "备料计划单号")
    private String planStockListOrderCode;

    /**
     * 仓库ID
     */
    @Transient
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

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
     * 出库用户
     */
    @Transient
    @ApiModelProperty(name="deliveryUserName",value = "出库用户")
    private String deliveryUserName;

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
