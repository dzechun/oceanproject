package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmMatching;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class MesPmMatchingDto extends MesPmMatching implements Serializable {

    /**
     * 产品Id
     */
    @ApiModelProperty(name="materialId" ,value="产品Id")
    @Transient
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Transient
    private String materialCode;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 主单位
     */
    @Transient
    @ApiModelProperty(name="mainUnit" ,value="主单位")
    private String mainUnit;

    /**
     * 流程单ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "流程单ID")
    @Transient
    private Long workOrderCardPoolId;

    /**
     * 产品型号
     */
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    @Transient
    private String productModuleName;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Transient
    private String workOrderCode;

    /**
     * 流转卡号
     */
    @ApiModelProperty(name = "workOrderCardId",value = "流转卡号")
    @Transient
    private String workOrderCardId;
}
