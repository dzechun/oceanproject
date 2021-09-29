package com.fantechs.common.base.general.dto.esop;

import com.fantechs.common.base.general.entity.esop.EsopWiBom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EsopWiBomDto extends EsopWiBom implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    private String materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    @Transient
    private String materialVersion;

    /**
     * 体积
     */
    @ApiModelProperty(name="specifications" ,value="体积")
    @Transient
    private String specifications;
}
