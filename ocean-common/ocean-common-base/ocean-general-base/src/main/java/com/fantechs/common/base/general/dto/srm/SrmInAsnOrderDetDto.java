package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SrmInAsnOrderDetDto extends SrmInAsnOrderDet implements Serializable {

    /**
     * ASN单号
     */
    @ApiModelProperty(name="asnCode",value = "ASN单号")
    private String asnCode;

    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    private String purchaseOrderCode;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    private Date orderDate;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    private String materialVersion;


    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    private String mainUnit;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 累计交货数量
     */
    @ApiModelProperty(name="totalDeliveryQty",value = "累计交货数量")
    private BigDecimal totalDeliveryQty;

    /**
     * 采购数量
     */
    @ApiModelProperty(name="orderQty",value = "采购数量")
    private BigDecimal orderQty;

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
     * 下发数量
     */
    @Transient
    @ApiModelProperty(name="issueQty",value = "下发数量")
    private BigDecimal issueQty;

    /**
     * 供应商ID
     */
    @Transient
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

}
