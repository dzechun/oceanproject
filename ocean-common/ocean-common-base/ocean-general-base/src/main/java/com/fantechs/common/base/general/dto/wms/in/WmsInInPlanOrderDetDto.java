package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInInPlanOrderDetDto extends WmsInInPlanOrderDet implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    private String materialVersion;


    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    private String mainUnit;

    /**
     * 库存状态
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态")
    private Long inventoryStatusName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;
}
