package com.fantechs.common.base.general.dto.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @Auther: bingo.ren
 * @Date: 2021/2/1 13:40
 * @Description: 产品及部件
 * @Version: 1.0
 */
@Data
public class MaterialAndPartsDTO {
    /**
     * 部件名称
     */
    @Transient
    @ApiModelProperty(name="partsInformationName" ,value="部件名称")
    private String partsInformationName;

    /**
     * 物料ID
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料料号")
    private String materialCode;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;
    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    private String productModuleName;
    /**
     * 包装单位-名称
     */
    @Transient
    @ApiModelProperty(name = "packingUnitName",value = "包装单位-名称")
    private String packingUnitName;
}
