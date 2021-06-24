package com.fantechs.common.base.general.dto.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsInAsnOrderDto extends WmsInAsnOrder implements Serializable {
    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 销退订单号
     */
    @Transient
    @ApiModelProperty(name = "salesReturnOrderCode",value = "销退订单号")
    private String salesReturnOrderCode;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 供应商
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商")
    private String supplierName;

    /**
     * 单据名称
     */
    @Transient
    @ApiModelProperty(name = "orderTypeName",value = "单据名称")
    private String orderTypeName;

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

    /**
     * 总数量
     */
    @Transient
    @ApiModelProperty(name="packingQty",value = "总数量")
    @Excel(name = "包装数量", height = 20, width = 30,orderNum="")
    private BigDecimal packingQty;

    /**
     * 实收数量
     */
    @Transient
    @ApiModelProperty(name="actualQty",value = "实收数量")
    @Excel(name = "实收数量", height = 20, width = 30,orderNum="")
    private BigDecimal actualQty;

    /**
     * 上架数量
     */
    @ApiModelProperty(name="putawayQty",value = "上架数量")
    @Excel(name = "上架数量", height = 20, width = 30,orderNum="")
    private BigDecimal putawayQty;
}
