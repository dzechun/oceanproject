package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/15
 */
@Data
public class SearchOmPurchaseReturnOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name="purchaseReturnOrderCode",value = "采退订单号")
    private String purchaseReturnOrderCode;

    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(name="makeOrderUserName",value = "制单人员")
    private String makeOrderUserName;

    @ApiModelProperty(name="returnDeptName",value = "退货部门")
    private String returnDeptName;

    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;
}
