package com.fantechs.common.base.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.basic.SmtPackageSpecification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SmtPackageSpecificationDto extends SmtPackageSpecification implements Serializable {
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="11")
    @Transient
    private String packingUnitName;

    /**
     * 包装单位描述
     */
    @ApiModelProperty(name="packingUnitDesc",value = "包装单位描述")
    @Excel(name = "包装单位描述", height = 20, width = 30,orderNum="12")
    @Transient
    private String packingUnitDesc;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Transient
    @Excel(name = "包装单位描述", height = 20, width = 30,orderNum="5")
    private String materialCode;


    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Transient
    @Excel(name = "包装单位描述", height = 20, width = 30,orderNum="6")
    private String materialDesc;

    /**
     * 条码规则
     */
    @ApiModelProperty(name="barcodeRule",value = "条码规则")
    @Excel(name = "条码规则", height = 20, width = 30,orderNum="13")
    @Transient
    private String barcodeRule;
}
