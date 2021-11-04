package com.fantechs.common.base.general.dto.eng;

import com.fantechs.common.base.general.entity.eng.EngMaterialMaintainLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;


@Data
public class EngMaterialMaintainLogDto extends EngMaterialMaintainLog implements Serializable {
    /**
     * 材料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 材料名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "材料名称")
    private String materialName;

    /**
     * 规格
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "规格")
    private String materialDesc;

    /**
     * 操作人
     */
    @Transient
    @ApiModelProperty(name="operatorUserName",value = "操作人")
    private String operatorUserName;

    private static final long serialVersionUID = 1L;
}
