package com.fantechs.common.base.general.entity.srm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchSrmPackingOrder extends BaseQuery implements Serializable {
    /**
     * 供应商编码
     */
    @ApiModelProperty(name = "supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    private String supplierName;


    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    private String despatchBatch;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderTime",value = "订单日期")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date orderTime;


    /**
     * 出厂时间
     */
    @ApiModelProperty(name="leaveFactoryTime",value = "出厂时间")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date leaveFactoryTime;

    /**
     * 离港时间
     */
    @ApiModelProperty(name="leavePortTime",value = "离港时间")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date leavePortTime;

    /**
     * 到港时间
     */
    @ApiModelProperty(name="arrivalPortTime",value = "到港时间")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date arrivalPortTime;

    /**
     * 到场时间
     */
    @ApiModelProperty(name="arrivalTime",value = "到场时间")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date arrivalTime;

    /**
     * 订单状态(1-未审核 2-审核中 3-已通过 4-未通过)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-未审核 2-审核中 3-已通过 4-未通过)")
    private Byte orderStatus;


    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    private String packingOrderCode;

}
