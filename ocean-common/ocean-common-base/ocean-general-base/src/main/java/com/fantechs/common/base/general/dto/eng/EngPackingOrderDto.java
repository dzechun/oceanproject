package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class EngPackingOrderDto extends EngPackingOrder implements Serializable {

    /**
     * 供应商编码
     */
    @Transient
    @ApiModelProperty(name = "supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="5")
    private String supplierName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;


    /**
     * 物流商名称
     */
    @Transient
    @ApiModelProperty(name="shipmentEnterpriseName",value = "物流商名称")
    private String shipmentEnterpriseName;

    /**
     * 到货登记人名称
     */
    @Transient
    @ApiModelProperty(name = "agoConfirmUserName",value = "到货登记人名称")
    private String agoConfirmUserName;

    /**
     * 总数量
     */
    @Transient
    @ApiModelProperty(name = "totalQty",value = "总数量")
    private BigDecimal totalQty;

    /**
     * 分配数量
     */
    @Transient
    @ApiModelProperty(value = "分配数量",name = "totalDistributionQty")
    private BigDecimal totalDistributionQty;

    /**
     * 上架数量
     */
    @Transient
    @ApiModelProperty( value = "上架数量",name = "totalPutawayQty")
    private BigDecimal totalPutawayQty;

    /**
     * 收货数量
     */
    @Transient
    @ApiModelProperty(value = "收货数量",name = "totalReceivingQty")
    private BigDecimal totalReceivingQty;
}
