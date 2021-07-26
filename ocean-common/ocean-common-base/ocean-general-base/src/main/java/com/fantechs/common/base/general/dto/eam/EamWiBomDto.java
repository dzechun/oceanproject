package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class EamWiBomDto extends EamWiBom implements Serializable {

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    private String materialName;

    /**
     * 版本
     */
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Transient
    private String materialVersion;

    /**
     * 规格
     */
    @ApiModelProperty(name="specifications" ,value="规格")
    @Transient
    private String specifications;
}
