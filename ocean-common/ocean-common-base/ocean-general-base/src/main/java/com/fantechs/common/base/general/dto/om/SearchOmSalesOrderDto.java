package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchOmSalesOrderDto extends BaseQuery implements Serializable {
    /**
     * 销售订单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
    private String salesOrderCode;
    /**
     * 客户订单号
     */
    @ApiModelProperty(value = "客户订单号", example = "客户订单号")
    private String customerOrderCode;
    /**
     * 合同号
     */
    @ApiModelProperty(value = "合同号", example = "合同号")
    private String contractCode;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "合同号", example = "合同号")
    private Long workOrderId;

    @ApiModelProperty(name = "orderType",value = "订单类型")
    private String orderType;

    @ApiModelProperty(name = "salesUserName",value = "销售人员")
    private String salesUserName;

    /**
     * 制单人员
     */
    @ApiModelProperty(name="createUserName",value = "制单人员")
    private String createUserName;

    /**
     * 审核人员
     */
    @ApiModelProperty(name="auditUserName",value = "审核人员")
    private String auditUserName;
}
