package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/15
 */
@Data
public class OmTransferOrderDto extends OmTransferOrder implements Serializable {

    /**
     * 订单总数量
     */
    @Transient
    @ApiModelProperty(name="totalQty",value = "订单总数量")
    @Excel(name = "订单总数量", height = 20, width = 30,orderNum="6")
    private BigDecimal totalQty;

    /**
     * 上架总数量
     */
    @Transient
    @ApiModelProperty(name="totalActualInQty",value = "上架总数量")
    @Excel(name = "上架总数量", height = 20, width = 30)
    private BigDecimal totalActualInQty;

    /**
     * 出货总数量
     */
    @Transient
    @ApiModelProperty(name="totalActualOutQty",value = "出货总数量")
    @Excel(name = "出货总数量", height = 20, width = 30)
    private BigDecimal totalActualOutQty;

    /**
     *调出仓库
     */
    @Transient
    @ApiModelProperty(name = "outWarehouseName",value = "调出仓库")
    @Excel(name = "调出仓库", height = 20, width = 30,orderNum="4")
    private String outWarehouseName;

    /**
     *调入仓库
     */
    @Transient
    @ApiModelProperty(name = "inWarehouseName",value = "调入仓库")
    @Excel(name = "调入仓库", height = 20, width = 30,orderNum="3")
    private String inWarehouseName;

    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    @Excel(name = "创建名称", height = 20, width = 30,orderNum="11")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="13")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;
}
