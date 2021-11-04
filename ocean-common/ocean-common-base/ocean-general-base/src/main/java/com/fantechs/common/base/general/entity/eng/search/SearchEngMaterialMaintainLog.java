package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchEngMaterialMaintainLog extends BaseQuery implements Serializable {

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name="contractQtyOrderId",value = "合同量单ID")
    private Long contractQtyOrderId;

    /**
     * 材料编码
     */
    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 材料名称
     */
    @ApiModelProperty(name="materialName",value = "材料名称")
    private String materialName;

    /**
     * 规格
     */
    @ApiModelProperty(name="materialDesc",value = "规格")
    private String materialDesc;

    /**
     * 维护信息
     */
    @ApiModelProperty(name="maintainContent",value = "维护信息")
    private String maintainContent;

    /**
     * 操作人id
     */
    @ApiModelProperty(name="operatorUserId",value = "操作人id")
    private Long operatorUserId;

    private static final long serialVersionUID = 1L;
}
