package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsOutDeliveryOrder extends BaseQuery implements Serializable {

    /**
     * 出库单ID
     */
    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    private Long deliveryOrderId;

    /**
     * 单据类型ID（1-销售出库单 2-调拨出库单）
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    private Long orderTypeId;

    /**
     * 销售出库单号
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "销售出库单号")
    private String deliveryOrderCode;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 相关单号1
     */
    @ApiModelProperty(name="relatedOrderCode1",value = "相关单号1")
    private String relatedOrderCode1;

    /**
     * 收货人
     */
    @ApiModelProperty(name="consignee" ,value="收货人")
    private String consignee;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkManName" ,value="联系人")
    private String linkManName;

    /**
     * 联系方式
     */
    @ApiModelProperty(name="linkManPhone" ,value="联系方式")
    private String linkManPhone;

    /**
     * 传真
     */
    @ApiModelProperty(name="faxNumber" ,value="传真")
    private String faxNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(name="emailAddress" ,value="邮箱")
    private String emailAddress;

    /**
     * 单据状态(1-待拣货，2-拣货中，3-待发运、4-部分发运、5-发运完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待拣货，2-拣货中，3-待发运、4-部分发运、5-发运完成)")
    private Integer orderStatus;

    @ApiModelProperty(name = "customerName",value = "客户")
    private String customerName;

    /**
     * 审核状态(0-未审核 1-已审核)
     */
    @ApiModelProperty(name="auditStatus",value = "审核状态(0-未审核 1-已审核)")
    private Byte auditStatus;

    @ApiModelProperty(name ="option1",value = "领料单类型")
    private String option1;

    @ApiModelProperty(name ="pickMaterialUserName",value = "领料人名称")
    private String pickMaterialUserName;

    @ApiModelProperty(name ="auditUserName",value = "审批人名称")
    private String auditUserName;

    /**
     * 月台名称
     */
    @ApiModelProperty(name = "platformName",value = "月台")
    private String platformName;

    /**
     * 是否创建作业单
     */
    @ApiModelProperty(name = "是否创建作业单 1-是 0-否",value = "ifCreatedJobOrder")
    private Byte ifCreatedJobOrder;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;
}
