package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmPoExpedite;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SrmPoExpediteDto extends SrmPoExpedite implements Serializable {

    /**
     * 采购订单号
     */
    @Transient
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30,orderNum="1")
    private String purchaseOrderCode;

    /**
     * 单据状态
     */
    @Transient
    @ApiModelProperty(name="orderStatus",value = "单据状态")
    @Excel(name = "单据状态", height = 20, width = 30,orderNum="2")
    private Byte orderStatus;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 供应商编码
     */
    @Transient
    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    /**
     * 订单日期
     */
    @Transient
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    /**
     * 采购部门
     */
    @Transient
    @ApiModelProperty(name="purchaseDeptName",value = "采购部门")
    @Excel(name = "采购部门", height = 20, width = 30,orderNum="5")
    private String purchaseDeptName;

    /**
     * 制单人
     */
    @Transient
    @ApiModelProperty(name="makeOrderUserName",value = "制单人")
    @Excel(name = "制单人", height = 20, width = 30,orderNum="6")
    private String makeOrderUserName;

    /**
     * 制单日期
     */
    @Transient
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Excel(name = "制单日期", height = 20, width = 30,orderNum="7")
    private Date makeOrderDate;

    /**
     * 备注说明
     */
    @Transient
    @ApiModelProperty(name="orderRemark",value = "备注说明")
    @Excel(name = "创建用备注说明户名称", height = 20, width = 30,orderNum="8")
    private String orderRemark;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    @Transient
    @ApiModelProperty(name="eMail",value = "供应商邮箱")
    private String eMail;

    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商手机号")
    private String mobilePhone;

    @Transient
    @ApiModelProperty(name="telephone",value = "供应商联系电话")
    private String telephone;

    @Transient
    @ApiModelProperty(name="completeDetail",value = "供应商地址")
    private String completeDetail;


    private static final long serialVersionUID = 1L;

}
