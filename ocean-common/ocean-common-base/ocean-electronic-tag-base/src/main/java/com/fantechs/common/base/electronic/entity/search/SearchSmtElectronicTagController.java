package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtElectronicTagController extends BaseQuery implements Serializable {

    /**
     * 电子标签控制器编码
     */
    @ApiModelProperty(name="electronicTagControllerCode",value = "电子标签控制器编码")
    private String electronicTagControllerCode;

    /**
     * 电子标签控制器名称
     */
    @ApiModelProperty(name="electronicTagControllerName",value = "电子标签控制器名称")
    private String electronicTagControllerName;

    /**
     * 电子标签控制器描述
     */
    @ApiModelProperty(name="electronicTagControllerDesc",value = "电子标签控制器描述")
    private String electronicTagControllerDesc;

    /**
     * 电子标签控制器ip
     */
    @ApiModelProperty(name="electronicTagControllerIp",value = "电子标签控制器ip")
    private String electronicTagControllerIp;

    /**
     * 电子标签控制器端口
     */
    @ApiModelProperty(name="electronicTagControllerPort",value = "电子标签控制器端口")
    private String electronicTagControllerPort;
}
