package com.fantechs.common.base.general.dto.wms.out;

import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Data
public class WmsOutDespatchOrderReJoReDetDto extends WmsOutDespatchOrderReJoReDet implements Serializable {

    @Transient
    @ApiModelProperty("装车单")
    private String despatchOrderCode;
    @Transient
    @ApiModelProperty("产品编码")
    private String materialName;

    @Transient
    @ApiModelProperty("车型")
    private String carType;

    @Transient
    @ApiModelProperty("车牌号")
    private String carNumber;

    @Transient
    @ApiModelProperty("司机")
    private String driverName;

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
