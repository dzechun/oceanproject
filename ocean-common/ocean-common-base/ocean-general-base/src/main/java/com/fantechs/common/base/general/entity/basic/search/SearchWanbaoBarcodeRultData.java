package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWanbaoBarcodeRultData extends BaseQuery implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 电压
     */
    @ApiModelProperty(name="voltage",value = "电压")
    private String voltage;

    /**
     * 客户型号编码
     */
    @ApiModelProperty(name = "customerModel", value = "客户型号编码")
    private String customerModel;

    /**
     * 识别码
     */
    @ApiModelProperty(name="identificationCode",value = "识别码")
    private String identificationCode;

    @ApiModelProperty(name="dataStatus",value = "状态")
    private String dataStatus;
}
