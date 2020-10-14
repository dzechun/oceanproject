package com.fantechs.common.base.entity.apply.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSmtOrder extends BaseQuery implements Serializable {

    /**
     * 订单号
     */
    @Column(name = "order_code")
    private String orderCode;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="customerName" ,value="客户名称")
    private String customerName;
}
