package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class EngPackingOrderSummaryDetDto extends EngPackingOrderSummaryDet implements Serializable {

    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    @Excel(name = "装箱单号", height = 20, width = 30,orderNum="1")
    private String packingOrderCode;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="2")
    private String supplierName;

    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatchs",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="3")
    private String despatchBatchs;

    /**
     * 请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    @Excel(name = "请购单号", height = 20, width = 30,orderNum="4")
    private String purchaseReqOrderCode;


    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="5")
    private String contractCode;

    /**
     * 专业名称
     */
    @ApiModelProperty(name="professionName",value = "专业")
    @Excel(name = "专业", height = 20, width = 30,orderNum="6")
    private String professionName;


    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="12")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "货物名称", height = 20, width = 30,orderNum="15")
    private String materialName;


    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 收货库位名称
     */
    @Transient
    @ApiModelProperty(name = "receivingStorageName",value = "收货库位名称")
    private String receivingStorageName;

    /**
     * 收货库位编码
     */
    @Transient
    @ApiModelProperty(name = "receivingStorageCode",value = "收货库位编码")
    private String receivingStorageCode;

    /**
     * 上架库位名称
     */
    @Transient
    @ApiModelProperty(name = "putawayStorageName",value = "上架库位名称")
    private String putawayStorageName;

    /**
     * 上架库位编码
     */
    @Transient
    @ApiModelProperty(name = "putawayStorageCode",value = "上架库位编码")
    private String putawayStorageCode;

    /**
     * 取消数量
     */
    @Transient
    @ApiModelProperty(name = "cancelQty",value = "取消数量")
    private BigDecimal cancelQty;

    /**
     * 按钮类型（1-确认 2-收货确认）
     */
    @Transient
    @ApiModelProperty(name = "buttonType",value = "按钮类型（1-确认 2-收货确认）")
    private Byte buttonType;

    /**
     * 装箱清单id
     */
    @ApiModelProperty(name = "packingOrderId",value = "装箱清单id")
    private Long packingOrderId;

    /**
     * 导出数量，要求为三位小数
     */
    @ApiModelProperty(name="exportQty",value = "导出数量")
    @Transient
    @Excel(name = "数量", height = 20, width = 30,orderNum="17")
    private BigDecimal exportQty;

    private Byte isCal;
}
