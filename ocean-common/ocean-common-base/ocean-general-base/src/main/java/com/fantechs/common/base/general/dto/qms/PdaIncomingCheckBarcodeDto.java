package com.fantechs.common.base.general.dto.qms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * @date 2020-12-28 15:15:07
 */
@Data
public class PdaIncomingCheckBarcodeDto implements Serializable {

    /**
     * 产品条码
     */
    @ApiModelProperty(name="barcode",value = "产品条码")
    @NotBlank(message = "产品条码不能为空")
    private String barcode;

    /**
     * 来料检验单明细id列表
     */
    @ApiModelProperty(name="incomingInspectionOrderDetIdList",value = "来料检验单明细id列表")
    @NotEmpty(message = "来料检验单明细id列表不能为空")
    private List<Long> incomingInspectionOrderDetIdList;

}
