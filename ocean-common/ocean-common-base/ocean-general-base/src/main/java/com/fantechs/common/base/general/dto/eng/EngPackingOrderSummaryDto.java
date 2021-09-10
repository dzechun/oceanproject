package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class EngPackingOrderSummaryDto extends EngPackingOrderSummary implements Serializable {


    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    @Column(name = "material_name")
    private String materialName;


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
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;


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
