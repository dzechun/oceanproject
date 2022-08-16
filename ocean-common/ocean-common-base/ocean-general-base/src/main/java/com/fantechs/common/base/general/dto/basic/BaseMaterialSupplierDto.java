package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseMaterialSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseMaterialSupplierDto extends BaseMaterialSupplier implements Serializable {

    private static final long serialVersionUID = -8520931395746280600L;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum = "1")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @Transient
    @Excel(name = "物料版本", height = 20, width = 30,orderNum = "2")
    private String materialVersion;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum = "3")
    private String materialDesc;

    /**
     * 客户名称
     */
    @Column(name = "supplier_name")
    @ApiModelProperty(name = "supplierName",value = "客户名称")
    @Excel(name = "客户名称", height = 20, width = 30,orderNum = "5")
    private String supplierName;

    /**
     * 客户编码
     */
    @Column(name = "supplier_code")
    @ApiModelProperty(name = "supplierCode",value = "客户编码")
    @Excel(name = "客户编码", height = 20, width = 30,orderNum = "6")
    private String supplierCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
