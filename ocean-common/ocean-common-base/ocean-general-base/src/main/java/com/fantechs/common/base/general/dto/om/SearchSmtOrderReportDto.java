package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.dto.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

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

    @ApiModelProperty(name = "contractDateT",value = "合同交期开始")
    private String contractDateT;

    @ApiModelProperty(name = "contractDateF",value = "合同交期结束")
    private String contractDateF;
}
