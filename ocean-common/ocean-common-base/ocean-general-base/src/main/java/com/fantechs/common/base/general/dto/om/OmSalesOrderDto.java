package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class OmSalesOrderDto extends OmSalesOrder implements Serializable {
    /**
     * 客户名
     */
    @ApiModelProperty(name="supplierName", value="客户名")
    @Excel(name = "供应商(客户)名称", height = 20, width = 30, orderNum = "2")
    private String supplierName;

    /**
     * 条码规则集合名称
     */
    @ApiModelProperty(name="barcodeRuleSetName", value = "条码规则集合名称")
    @Excel(name = "条码规则集合编码", height = 20, width = 30, orderNum = "16")
    private String barcodeRuleSetName;

    /**
     * 表体相关
     */
    @ApiModelProperty(name="omSalesOrderDetDtoList", value="表体")
//    @Excel(name = "表体")
    private List<OmSalesOrderDetDto> omSalesOrderDetDtoList;

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30, orderNum = "18")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30, orderNum = "20")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
//    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

}
