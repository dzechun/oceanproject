package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 运输地信息
 */
@Data
public class TransportInformation extends ValidGroup implements Serializable {

    /**
     * 拣货单号
     */
    @ApiModelProperty(name="code",value = "拣货单号")
    private String code;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name="shopName",value = "店铺名称")
    private String shopName;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="shipToName",value = "客户名称")
    private String shipToName;

    /**
     * 省市区
     */
    @ApiModelProperty(name="provinces",value = "省市区")
    private String provinces;
}
