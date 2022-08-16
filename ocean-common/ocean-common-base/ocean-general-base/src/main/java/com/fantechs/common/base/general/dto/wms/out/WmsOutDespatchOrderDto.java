package com.fantechs.common.base.general.dto.wms.out;

import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Data
public class WmsOutDespatchOrderDto extends WmsOutDespatchOrder implements Serializable {
    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;


    /**
     * 物流商
     */
    @Transient
    @ApiModelProperty(name="shipmentEnterpriseName",value = "物流商")
    private String shipmentEnterpriseName;

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
