package com.fantechs.common.base.general.dto.qms;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class QmsAndinStorageQuarantineDto extends QmsAndinStorageQuarantine implements Serializable {

    /**
     * 生产工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "生产工单号")
    @Excel(name = "生产工单号", height = 20, width = 30,orderNum="13")
    private String workOrderCode;

    /**
     * 栈板码
     */
    @Transient
    @ApiModelProperty(name = "palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30,orderNum="13")
    private String palletCode;

    /**
     * 料号编码
     */
    @Transient
    @ApiModelProperty(name = "productCode",value = "料号编码")
    @Excel(name = "料号编码", height = 20, width = 30,orderNum="13")
    private String productCode;

    /**
     * 料号描述
     */
    @Transient
    @ApiModelProperty(name = "productDesc",value = "料号描述")
    @Excel(name = "料号描述", height = 20, width = 30,orderNum="13")
    private String productDesc;

    /**
     * 区域名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "区域名称")
    @Excel(name = "区域名称", height = 20, width = 30,orderNum="13")
    private String warehouseAreaName;

    /**
     * 区域码
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaCode",value = "区域码")
    private String warehouseAreaCode;

    /**
     * 箱数
     */
    @Transient
    @ApiModelProperty(name = "cartons",value = "箱数")
    @Excel(name = "箱数", height = 20, width = 30,orderNum="13")
    private BigDecimal cartons;

    /**
     * 入库数量
     */
    @Transient
    @ApiModelProperty(name = "inventoryQuantity",value = "入库数量")
    @Excel(name = "入库数量", height = 20, width = 30,orderNum="13")
    private BigDecimal inventoryQuantity;

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

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
