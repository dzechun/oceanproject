package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmHtTransferOrder;
import com.fantechs.common.base.general.entity.om.OmHtTransferOrderDet;
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
public class OmHtTransferOrderDto extends OmHtTransferOrderDet implements Serializable {
    /**
     * 订单总数量
     */
    @Transient
    @ApiModelProperty(name="totalQty",value = "订单总数量")
    @Excel(name = "订单总数量", height = 20, width = 30,orderNum="6")
    private BigDecimal totalQty;

    /**
     * 货主
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    @Excel(name = "货主", height = 20, width = 30,orderNum="3")
    private String materialOwnerName;

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
    @Excel(name = "调入仓库", height = 20, width = 30,orderNum="5")
    private String inWarehouseName;

    /**
     * 总数量
     */
    @Transient
    @ApiModelProperty(name = "countOrderQty",value = "总数量")
    private BigDecimal countOrderQty;

    /**
     * 总体积
     */
    @Transient
    @ApiModelProperty(name = "countVolume",value = "总体积")
    private BigDecimal countVolume;

    /**
     * 总净重
     */
    @Transient
    @ApiModelProperty(name = "countNetWeight",value = "总净重")
    private BigDecimal countNetWeight;

    /**
     * 总毛重
     */
    @Transient
    @ApiModelProperty(name = "countGrossWeight",value = "总毛重")
    private BigDecimal countGrossWeight;

    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    @Excel(name = "创建名称", height = 20, width = 30,orderNum="13")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="15")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="17")
    private String organizationName;
}
