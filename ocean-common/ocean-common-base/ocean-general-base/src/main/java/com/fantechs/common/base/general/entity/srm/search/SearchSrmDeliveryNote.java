package com.fantechs.common.base.general.entity.srm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SearchSrmDeliveryNote extends BaseQuery implements Serializable {

    /**
     * ASN编码
     */
    @ApiModelProperty(name="asnCode",value = "ASN编码")
    private String asnCode;

    /**
     * 客户订单ID
     */
    @ApiModelProperty(name="orderId",value = "客户订单ID")
    private Long orderId;


    /**
     * 客户代码
     */
    @ApiModelProperty(name="customerCode",value = "客户代码")
    private String customerCode;


    private static final long serialVersionUID = 1L;
}
