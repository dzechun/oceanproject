package com.fantechs.common.base.general.dto.wms.out.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsOutDeliveryOrderImport implements Serializable {

    /**
     * 组号(必填)
     */
    @Excel(name = "组号(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="groupCode" ,value="组号(必填)")
    private String groupCode;

    /**
     * 货主编码(必填)
     */
    @Excel(name = "货主编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="materialOwnerCode" ,value="货主编码(必填)")
    private String materialOwnerCode;

    /**
     * 货主id
     */
    @ApiModelProperty(name="materialOwnerId" ,value="货主id")
    private Long materialOwnerId;

    /**
     * 客户编码
     */
    @Excel(name = "客户编码",  height = 20, width = 30)
    @ApiModelProperty(name="customerCode" ,value="客户编码")
    private String customerCode;

    /**
     * 客户id
     */
    @ApiModelProperty(name="customerId" ,value="客户id")
    private Long customerId;

    /**
     * 收货人编码
     */
    @Excel(name = "收货人编码",  height = 20, width = 30)
    @ApiModelProperty(name="consigneeCode" ,value="收货人编码")
    private String consigneeCode;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name="consignee" ,value="收货人名称")
    private String consignee;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName" ,value="联系人名称")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone" ,value="联系人电话")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber" ,value="传真号码")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="emailAddress" ,value="邮箱地址")
    private String emailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="detailedAddress" ,value="详细地址")
    private String detailedAddress;

    /**
     * 订单日期(yyyy-MM-dd)(必填)
     */
    @Excel(name = "订单日期(yyyy-MM-dd)(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="orderDate" ,value="订单日期(yyyy-MM-dd)(必填)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    /**
     * 要求到达日期(yyyy-MM-dd)(必填)
     */
    @Excel(name = "要求到达日期(yyyy-MM-dd)(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="demandArriveDate" ,value="要求到达日期(yyyy-MM-dd)(必填)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date demandArriveDate;

    /**
     * 预计发运日期(yyyy-MM-dd)(必填)
     */
    @Excel(name = "预计发运日期(yyyy-MM-dd)(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="planDespatchDate" ,value="预计发运日期(yyyy-MM-dd)(必填)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDespatchDate;

    /**
     * 相关单号
     */
    @Excel(name = "相关单号",  height = 20, width = 30)
    @ApiModelProperty(name="relatedOrderCode1" ,value="相关单号")
    private String relatedOrderCode1;

    /**
     * 备注
     */
    @Excel(name = "备注",  height = 20, width = 30)
    @ApiModelProperty(name="remark" ,value="备注")
    private String remark;


    //-------------明细----------------

    /**
     * 发货库位编码
     */
    @Excel(name = "发货库位编码",  height = 20, width = 30)
    @ApiModelProperty(name="storageCode" ,value="发货库位编码")
    private String storageCode;

    /**
     * 库位id
     */
    @ApiModelProperty(name="storageId" ,value="库位id")
    private Long storageId;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库id")
    private Long warehouseId;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码",  height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId" ,value="物料id")
    private Long materialId;

    /**
     * 包装数量
     */
    @Excel(name = "包装数量",  height = 20, width = 30)
    @ApiModelProperty(name="packingQty" ,value="包装数量")
    private BigDecimal packingQty;

    /**
     * 批次号
     */
    @Excel(name = "批次号",  height = 20, width = 30)
    @ApiModelProperty(name="batchCode" ,value="批次号")
    private String batchCode;

    /**
     * LID号
     */
    @Excel(name = "LID号",  height = 20, width = 30)
    @ApiModelProperty(name="LIDCode" ,value="LID号")
    private String LIDCode;

    /**
     * BR号
     */
    @Excel(name = "BR号",  height = 20, width = 30)
    @ApiModelProperty(name="BRCode" ,value="BR号")
    private String BRCode;
}
