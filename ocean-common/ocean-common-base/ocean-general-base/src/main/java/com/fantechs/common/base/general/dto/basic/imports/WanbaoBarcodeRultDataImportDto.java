package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WanbaoBarcodeRultDataImportDto implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="1")
    private String materialCode;


    /**
     * 识别码
     */
    @ApiModelProperty(name="identificationCode",value = "识别码")
    @Excel(name = "识别码", height = 20, width = 30,orderNum="4")
    private String identificationCode;


}
