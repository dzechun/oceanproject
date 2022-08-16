package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BasePackageSpecification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BasePackageSpecificationDto extends BasePackageSpecification implements Serializable {
//    /**
//     * 物料编码
//     */
//    @ApiModelProperty(name="materialCode" ,value="物料编码")
//    @Transient
//    @Excel(name = "物料编码", height = 20, width = 30,orderNum="5")
//    private String materialCode;
//
//
//    /**
//     * 物料描述
//     */
//    @ApiModelProperty(name="materialDesc" ,value="物料描述")
//    @Transient
//    @Excel(name = "物料描述", height = 20, width = 30,orderNum="6")
//    private String materialDesc;

    /**
     * 版本
     */
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Transient
    @Excel(name = "版本", height = 20, width = 30,orderNum="7")
    private String materialVersion;

//    /**
//     * 条码规则
//     */
//    @ApiModelProperty(name="barcodeRule",value = "条码规则")
//    @Excel(name = "条码规则", height = 20, width = 30,orderNum="8")
//    @Transient
//    private String barcodeRule;
//
//    /**
//     * 包装单位名称
//     */
//    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
//    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="9")
//    @Transient
//    private String packingUnitName;
//
//    /**
//     * 包装单位描述
//     */
//    @ApiModelProperty(name="packingUnitDesc",value = "包装单位描述")
//    @Excel(name = "包装单位描述", height = 20, width = 30,orderNum="10")
//    @Transient
//    private String packingUnitDesc;

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

//    /**
//     * 工序代码
//     */
//    @ApiModelProperty(name="processCode" ,value="工序代码")
//    @Excel(name = "工序代码", height = 20, width = 30)
//    private String processCode;
//
//    /**
//     * 工序名称
//     */
//    @ApiModelProperty(name="processName" ,value="工序名称")
//    @Excel(name = "工序名称", height = 20, width = 30)
//    private String processName;
//
//    /**
//     * 工序描述
//     */
//    @ApiModelProperty(name="processDesc" ,value="工序描述")
//    @Excel(name = "工序描述", height = 20, width = 30)
//    private String processDesc;
}
