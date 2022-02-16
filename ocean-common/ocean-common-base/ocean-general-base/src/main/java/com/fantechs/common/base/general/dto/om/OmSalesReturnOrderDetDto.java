package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/21
 */
@Data
public class OmSalesReturnOrderDetDto extends OmSalesReturnOrderDet implements Serializable {

    /**
     * 销售订单号
     */
    @Transient
    @ApiModelProperty(name = "salesOrderCode",value = "销售订单号")
    private String salesOrderCode;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;
    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    private String materialVersion;

    /**
     * 包装单位
     */
    @Transient
    @ApiModelProperty(name = "mainUnit",value = "包装单位")
    private String mainUnit;

    /**
     * 体积
     */
    @Transient
    @ApiModelProperty(name = "volume",value = "体积")
    private BigDecimal volume;

    /**
     * 净重
     */
    @Transient
    @ApiModelProperty(name = "netWeight",value = "净重")
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @Transient
    @ApiModelProperty(name = "grossWeight",value = "毛重")
    private BigDecimal grossWeight;
    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;

    /**
     * 销售订单数量
     */
    @ApiModelProperty(name="salesOrderQty",value = "销售订单数量")
    @Excel(name = "销售订单数量", height = 20, width = 30)
    private BigDecimal salesOrderQty;
}
