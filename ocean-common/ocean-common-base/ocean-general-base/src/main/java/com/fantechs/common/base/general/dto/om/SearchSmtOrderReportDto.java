package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/25
 */
@Data
public class SearchSmtOrderReportDto extends BaseQuery implements Serializable {
    /**
     * 订单号
     */
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode" ,value="合同号")
    private String contractCode;

    /**
     * 业务员名称
     */
    @ApiModelProperty(name="salesManName" ,value="业务员名称")
    private String salesManName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;
}
