package com.fantechs.common.base.general.dto.srm;

import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDetBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SrmInHtAsnOrderDetBarcodeDto extends SrmInHtAsnOrderDetBarcode implements Serializable {


    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    private String purchaseOrderCode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

}
