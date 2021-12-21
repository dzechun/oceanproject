package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OmPurchaseReturnOrderDto extends OmPurchaseReturnOrder implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30, orderNum = "6",needMerge = true)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30, orderNum = "8",needMerge = true)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
//    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30, orderNum = "2",needMerge = true)
    private String supplierName;

    /**
     * 制单人员
     */
    @Transient
    @ApiModelProperty(name = "makeOrderUserName",value = "制单人员")
    @Excel(name = "制单人员", height = 20, width = 30, orderNum = "3",needMerge = true)
    private String makeOrderUserName;

    /**
     * 退货部门
     */
    @Transient
    @ApiModelProperty(name = "returnDeptName",value = "退货部门")
    @Excel(name = "退货部门", height = 20, width = 30, orderNum = "4",needMerge = true)
    private String returnDeptName;

    /**
     * 采退订单明细
     */
    @ApiModelProperty(name = "omPurchaseReturnOrderDetDtos",value = "采退订单明细")
    @ExcelCollection(name="采退订单明细",orderNum="10")
    private List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos = new ArrayList<>();
}
