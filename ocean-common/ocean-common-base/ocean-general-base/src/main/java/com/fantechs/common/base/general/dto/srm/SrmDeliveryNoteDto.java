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
     * 采购订单客户代码
     */
    @Transient
    @ApiModelProperty(name="purchaseCustomerCode",value = "采购订单客户代码")
    @Excel(name = "采购订单客户代码", height = 20, width = 30,orderNum="3")
    private String purchaseCustomerCode;

    /**
     * 送货计划客户代码
     */
    @Transient
    @ApiModelProperty(name="deliveryCustomerCode",value = "送货计划客户代码")
    @Excel(name = "送货计划客户代码", height = 20, width = 30,orderNum="3")
    private String deliveryCustomerCode;

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

    private static final long serialVersionUID = 1L;
}
