package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/23
 */
@Data
public class OmOtherOutOrderDetDto extends OmOtherOutOrderDet implements Serializable {
    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="1")
    private String warehouseName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 包装单位
     */
    @Transient
    @ApiModelProperty(name = "mainUnit",value = "包装单位")
    @Excel(name = "包装单位", height = 20, width = 30,orderNum="4")
    private String mainUnit;

    /**
     * 体积
     */
    @Transient
    @ApiModelProperty(name = "volume",value = "体积")
    @Excel(name = "体积", height = 20, width = 30,orderNum="6")
    private BigDecimal volume;

    /**
     * 净重
     */
    @Transient
    @ApiModelProperty(name = "netWeight",value = "净重")
    @Excel(name = "净重", height = 20, width = 30,orderNum="7")
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @Transient
    @ApiModelProperty(name = "grossWeight",value = "毛重")
    @Excel(name = "毛重", height = 20, width = 30,orderNum="8")
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
     * 其他出库订单号
     */
    @Transient
    @ApiModelProperty(name = "otherOutOrderCode",value = "其他出库订单号")
    private String otherOutOrderCode;

    /**
     * 客户订单号
     */
    @Transient
    @ApiModelProperty(name = "customerOrderCode",value = "客户订单号")
    private String customerOrderCode;

    /**
     * 下发数量
     */
    @ApiModelProperty(name="issueQty",value = "下发数量")
    @Transient
    private BigDecimal issueQty;
}
