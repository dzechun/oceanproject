package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutPurchaseReturnDto extends WmsOutPurchaseReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商代码
     */
    @ApiModelProperty(name="supplierCode" ,value="供应商代码")
    @Excel(name = "供应商代码", height = 20, width = 30,orderNum="2")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName" ,value="供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 采购员名称
     */
    @ApiModelProperty(name="buyName" ,value="采购员名称")
    @Excel(name = "采购员名称", height = 20, width = 30,orderNum="4")
    private String buyName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
