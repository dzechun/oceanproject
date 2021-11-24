package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmPoProductionInfo;
import com.fantechs.common.base.support.ValidGroup;
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
public class SrmPoProductionInfoDto extends SrmPoProductionInfo implements Serializable {
    /**
     * 采购订单号
     */
    @Transient
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30,orderNum="1")
    private String purchaseOrderCode;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="7")
    private String materialName;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="5")
    private String materialVersion;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="6")
    private String materialDesc;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="13")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="15")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}
