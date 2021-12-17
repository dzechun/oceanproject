package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Data
public class OmHtOtherOutOrderDto extends OmHtOtherOutOrder implements Serializable {
    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="4")
    private String supplierName;

    /**
     * 订单总数量
     */
    @Transient
    @ApiModelProperty(name="totalOrderQty",value = "订单总数量")
    @Excel(name = "订单总数量", height = 20, width = 30,orderNum="14")
    private BigDecimal totalOrderQty;

    /**
     * 出货总数量
     */
    @Transient
    @ApiModelProperty(name="totalActualQty",value = "出货总数量")
    @Excel(name = "出货总数量", height = 20, width = 30,orderNum="14")
    private BigDecimal totalActualQty;

    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    @Excel(name = "创建名称", height = 20, width = 30,orderNum="24")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="26")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    @Excel(name = "组织", height = 20, width = 30,orderNum="28")
    private String organizationName;
}
