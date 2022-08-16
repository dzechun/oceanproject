package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.BaseMaterialPackage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseMaterialPackageDto extends BaseMaterialPackage implements Serializable {

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 条码规则
     */
    @ApiModelProperty(name="barcodeRule",value = "条码规则")
    @Transient
    private String barcodeRule;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    @Transient
    private String packingUnitName;

    /**
     * 包装单位描述
     */
    @ApiModelProperty(name="packingUnitDesc",value = "包装单位描述")
    @Transient
    private String packingUnitDesc;
}
