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
     * 客户编码
     */
    @Excel(name = "客户编号",  height = 20, width = 30, orderNum="1")
    @ApiModelProperty(name="customerCode" ,value="客户编号")
    private String customerCode;

    /**
     * LID
     */
    @Excel(name = "LID",  height = 20, width = 30, orderNum="3")
    @ApiModelProperty(name="LIDCode" ,value="LID")
    private String LIDCode;

    /**
     * BR/NO
     */
    @Excel(name = "BR/NO",  height = 20, width = 30, orderNum="4")
    @ApiModelProperty(name="BRCode" ,value="BR/NO")
    private String BRCode;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30, orderNum="7")
    private String productModelCode;

    /**
     * 出货通知单生成规则
     */
    @Excel(name = "出货通知单生成规则",  height = 20, width = 30, orderNum="9")
    @ApiModelProperty(name="genRule" ,value="出货通知单生成规则")
    private String genRule;

    /**
     * 预计发运日期(yyyy-MM-dd)(必填)
     */
    @Excel(name = "计划发运时间",  height = 20, width = 30, orderNum="11")
    @ApiModelProperty(name="planDespatchDate" ,value="计划发运时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDespatchDate;

    //-------------明细----------------

    /**
     * 产品编码
     */
    @ApiModelProperty(name = "materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30, orderNum="5")
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum = "6")
    private String materialDesc;

    /**
     * 计划出货数量
     */
    @Excel(name = "计划出货数量",  height = 20, width = 30, orderNum="8")
    @ApiModelProperty(name="packingQty" ,value="计划出货数量")
    private BigDecimal packingQty;

    /**
     * PO号
     */
    @ApiModelProperty(name="option3",value = "PO号")
    @Excel(name = "PO号",  height = 20, width = 30, orderNum="2")
    private String option3;

    /**
     * 发货库位编码
     */
    @ApiModelProperty(name="storageCode" ,value="发货库位编码")
    @Excel(name = "发货库位编码",  height = 20, width = 30, orderNum="10")
    private String storageCode;



    // ------------ 默认数据 ------------
    /**
     * 货主id
     */
    @ApiModelProperty(name="materialOwnerId" ,value="货主id")
    private Long materialOwnerId;

    /**
     * 客户id
     */
    @ApiModelProperty(name="customerId" ,value="客户id")
    private Long customerId;

    /**
     * 收货人编码
     */
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
     * 物料id
     */
    @ApiModelProperty(name="materialId" ,value="物料id")
    private Long materialId;

}
