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
    @Excel(name = "货主", height = 20, width = 30,orderNum="2")
    private String materialOwnerName;

    /**
     * 来源订单号
     */
    @Transient
    @ApiModelProperty(name = "sourceOrderCode",value = "销退订单号")
    @Excel(name = "来源订单号", height = 20, width = 30,orderNum="3")
    private String sourceOrderCode;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="4")
    private String warehouseName;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name="storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="5")
    private String storageCode;

    /**
     * 供应商
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商")
    @Excel(name = "供应商", height = 20, width = 30,orderNum="6")
    private String supplierName;

    /**
     * 单据名称
     */
    @Transient
    @ApiModelProperty(name = "orderTypeName",value = "单据名称")
    @Excel(name = "单据名称", height = 20, width = 30,orderNum="7")
    private String orderTypeName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    @Excel(name = "组织", height = 20, width = 30,orderNum="8")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum="19")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="21")
    private String modifiedUserName;

    /**
     * 总数量
     */
    @Transient
    @ApiModelProperty(name="packingQty",value = "总数量")
    @Excel(name = "包装数量", height = 20, width = 30,orderNum="22")
    private BigDecimal packingQty;

    /**
     * 实收数量
     */
    @Transient
    @ApiModelProperty(name="actualQty",value = "实收数量")
    @Excel(name = "实收数量", height = 20, width = 30,orderNum="23")
    private BigDecimal actualQty;

    /**
     * 上架数量
     */
    @ApiModelProperty(name="putawayQty",value = "上架数量")
    @Excel(name = "上架数量", height = 20, width = 30,orderNum="24")
    private BigDecimal putawayQty;

}
