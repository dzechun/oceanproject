package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchBaseKeyMaterial extends BaseQuery implements Serializable {

    /**
     * 产品类别(2-产品型号 3-产品料号)
     */
    @ApiModelProperty(name="productType",value = "产品类别(2-产品型号 3-产品料号)")
    @Excel(name = "产品类别(2-产品型号 3-产品料号)", height = 20, width = 30)
    @Column(name = "product_type")
    private Byte productType;

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    private String productModelCode;


    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 工位名称
     */
    @ApiModelProperty(name = "stationName",value = "工位名称")
    private String stationName;
}
