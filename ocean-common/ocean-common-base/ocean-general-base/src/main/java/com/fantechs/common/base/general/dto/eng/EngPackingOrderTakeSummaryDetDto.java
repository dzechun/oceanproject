package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class EngPackingOrderTakeSummaryDetDto implements Serializable {

    /**
     * 装箱汇总明细ID
     */
    @ApiModelProperty(name="packingOrderSummaryDetId",value = "装箱汇总明细ID")
    private Long packingOrderSummaryDetId;

    /**
     * 装箱汇总ID
     */
    @ApiModelProperty(name="packingOrderSummaryId",value = "装箱汇总ID")
    private Long packingOrderSummaryId;

    /**
     * 包装箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包装箱号")
    @Excel(name = "包装箱号", height = 20, width = 30,orderNum="7")
    private String cartonCode;

    /**
     * 内箱号
     */
    @ApiModelProperty(name="innerCartonCode",value = "内箱号")
    @Excel(name = "内箱号", height = 20, width = 30,orderNum="8")
    private String innerCartonCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="9")
    private String locationNum;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30,orderNum="11")
    private String deviceCode;

    /**
     * 主项号
     */
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="10")
    private String dominantTermCode;


    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;


    /**
     * 图号
     */
    @ApiModelProperty(name="drawingNumber",value = "图号")
    @Excel(name = "图号", height = 20, width = 30,orderNum="13")
    private String drawingNumber;

    /**
     * 件号
     */
    @ApiModelProperty(name="partNumber",value = "件号")
    @Excel(name = "件号", height = 20, width = 30,orderNum="14")
    private String partNumber;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    // @Excel(name = "数量", height = 20, width = 30,orderNum="17")
    private BigDecimal qty;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位", height = 20, width = 30,orderNum="18")
    private String unitName;

    /**
     * 原材料编码
     */
    @ApiModelProperty(name="rawMaterialCode",value = "原材料编码")
    // @Excel(name = "原材料编码", height = 20, width = 30,orderNum="18")
    private String rawMaterialCode;


    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    private String despatchBatch;

    /**
     * 规格
     */
    @ApiModelProperty(name="spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="16")
    private String spec;


    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="19")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 分配数量
     */
    @ApiModelProperty(value = "分配数量",name = "distributionQty")
    private BigDecimal distributionQty;

    /**
     * 上架数量
     */
    @ApiModelProperty( value = "上架数量",name = "putawayQty")
    @Excel(name = "上架数量", height = 20, width = 30,orderNum="22")
    private BigDecimal putawayQty;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量",name = "receivingQty")
    @Excel(name = "收货数量", height = 20, width = 30,orderNum="21")
    private BigDecimal receivingQty;

    @ApiModelProperty(value = "收货库位id",name = "receivingStorageId")
    private Long receivingStorageId;


    @ApiModelProperty(name = "putawayStorageId",value = "上架库位")
    private Long putawayStorageId;

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
     * 收货库位
     */
    @Transient
    @ApiModelProperty(name = "receivingStorageName",value = "收货库位")
    private String receivingStorageName;

    /**
     * 上架库位
     */
    @Transient
    @ApiModelProperty(name = "putawayStorageCode",value = "上架库位")
    @Excel(name = "上架库位", height = 20, width = 30)
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
     *汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)
     */
    @ApiModelProperty(name = "summaryDetStatus",value = "汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)")
    @Excel(name = "汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)", height = 20, width = 30,replace = {"待收货_1","收货中_2","待上架_3","完成_4"})
    private Byte summaryDetStatus;

    /**
     * 导出数量，要求为三位小数
     */
    @ApiModelProperty(name="exportQty",value = "导出数量")
    @Transient
    @Excel(name = "数量", height = 20, width = 30,orderNum="17")
    private BigDecimal exportQty;

    private Byte isCal;
}
