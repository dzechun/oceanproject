package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerHtJobOrderDto extends WmsInnerHtJobOrder implements Serializable {
    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 单据类型
     */
    @Transient
    @ApiModelProperty(name = "orderTypeName",value = "单据类型")
    private String orderTypeName;

    /**
     * 工作人员
     */
    @Transient
    @ApiModelProperty(name = "workName",value = "工作人员")
    private String workName;

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

    /**
     * 是否栈板自动生成(1-是，0-否) PDA标识字段
     */
    @Transient
    @ApiModelProperty(name = "isPallet",value = "是否栈板自动生成(1-是，0-否) PDA标识字段")
    private Byte isPallet;

    /**
     * 月台名称
     */
    @Transient
    @ApiModelProperty(name = "platformName",value = "月台")
    private String platformName;
}
