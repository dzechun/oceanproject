package com.fantechs.provider.lizi.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/16
 */
@Data
public class SearchLiziScanBarcodeLog extends BaseQuery implements Serializable {

    private Long scanBarcodeLogId;

    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name = "idNumber",value = "ID码")
    private String idNumber;

    @ApiModelProperty(name = "skuCode",value = "SKU码")
    private String skuCode;

    @ApiModelProperty(name = "sixNineCode",value = "69码")
    private String sixNineCode;
}
