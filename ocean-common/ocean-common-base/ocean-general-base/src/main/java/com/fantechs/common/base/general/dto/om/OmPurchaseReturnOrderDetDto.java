package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OmPurchaseReturnOrderDetDto extends OmPurchaseReturnOrderDet implements Serializable {

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="2")
    private String warehouseName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="3")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="4")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="5")
    private String materialDesc;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="6")
    private String materialVersion;

    /**
     * 体积
     */
    @Transient
    @ApiModelProperty(name = "volume",value = "体积")
    @Excel(name = "体积", height = 20, width = 30,orderNum="7")
    private BigDecimal volume;

    /**
     * 净重
     */
    @Transient
    @ApiModelProperty(name = "netWeight",value = "净重")
    @Excel(name = "净重", height = 20, width = 30,orderNum="8")
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @Transient
    @ApiModelProperty(name = "grossWeight",value = "毛重")
    @Excel(name = "毛重", height = 20, width = 30,orderNum="9")
    private BigDecimal grossWeight;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    //@Excel(name = "创建账号", height = 20, width = 30, orderNum = "18")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    //@Excel(name = "修改账号", height = 20, width = 30, orderNum = "20")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    //@Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 采退订单号
     */
    @Transient
    @ApiModelProperty(name = "purchaseReturnOrderCode",value = "采退订单号")
    @Excel(name = "采退订单号", height = 20, width = 30,orderNum="1")
    private String purchaseReturnOrderCode;

    /**
     * 累计退货数量
     */
    @ApiModelProperty(name="totalSalesReturnQty",value = "累计退货数量")
    //@Excel(name = "累计退货数量", height = 20, width = 30,orderNum="")
    @Transient
    private BigDecimal totalSalesReturnQty;

    /**
     * 累计收货数量
     */
    @ApiModelProperty(name="totalReceivingQty",value = "累计收货数量")
    //@Excel(name = "累计收货数量", height = 20, width = 30,orderNum="")
    @Transient
    private BigDecimal totalReceivingQty;

    /**
     * 下发数量
     */
    @ApiModelProperty(name="issueQty",value = "下发数量")
    @Transient
    private BigDecimal issueQty;

    /**
     * 采退完成状态（0、未完成 1、已完成）
     */
    @ApiModelProperty(name="finishStatus",value = "采退完成状态（0、未完成 1、已完成）")
    @Transient
    private Byte finishStatus;

    /**
     * 供应商id
     */
    @ApiModelProperty(name="supplierId",value = "供应商id")
    @Transient
    private Long supplierId;
}
