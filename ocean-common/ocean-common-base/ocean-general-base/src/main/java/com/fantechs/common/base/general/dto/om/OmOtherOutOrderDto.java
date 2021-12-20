package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/23
 */
@Data
public class OmOtherOutOrderDto extends OmOtherOutOrder implements Serializable {
    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 订单总数量
     */
    @Transient
    @ApiModelProperty(name="totalOrderQty",value = "订单总数量")
    @Excel(name = "订单总数量", height = 20, width = 30,orderNum="7")
    private BigDecimal totalOrderQty;

    /**
     * 出货总数量
     */
    @Transient
    @ApiModelProperty(name="totalActualQty",value = "出货总数量")
    @Excel(name = "出货总数量", height = 20, width = 30,orderNum="8")
    private BigDecimal totalActualQty;

    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    @Excel(name = "创建名称", height = 20, width = 30,orderNum="16")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="18")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;
}
