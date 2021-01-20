package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNote;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SrmDeliveryNoteDto extends SrmDeliveryNote implements Serializable {

    /**
     * 订单客户代码
     */
    @Transient
    @ApiModelProperty(name="supplierCode",value = "客户代码")
    @Excel(name = "客户代码", height = 20, width = 30,orderNum="3")
    private String supplierCode;


    /**
     * 订单数量
     */
    @Transient
    @ApiModelProperty(name="orderQuantity",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="")
    private BigDecimal orderQuantity;

    /**
     * 计划交货日期
     */
    @Transient
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Excel(name = "计划交货日期", height = 20, width = 30,orderNum="4")
    private Date planDeliveryDate;


    /**
     * 需用时间
     */
    @Transient
    @ApiModelProperty(name="needTime",value = "需用时间")
    @Excel(name = "需用时间", height = 20, width = 30,orderNum="4")
    private Date needTime;

    /**
     * 送货员名称
     */
    @Transient
    @ApiModelProperty(name="deliverymanName",value = "送货员名称")
    @Excel(name = "送货员名称", height = 20, width = 30,orderNum="4")
    private String deliverymanName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
