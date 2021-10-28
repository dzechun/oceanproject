package com.fantechs.common.base.general.entity.leisai.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchLeisaiProductAndHalfOrder extends BaseQuery implements Serializable {

    /**
     * 单据号
     */
    @ApiModelProperty(name="processInputOrderCode",value = "单据号")
    private String processInputOrderCode;

    /**
     * 产品SN
     */
    @ApiModelProperty(name="productSn",value = "产品SN")
    private String productSn;

    /**
     * 生产订单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产订单")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="productMaterialCode",value = "产品料号")
    private String productMaterialCode;

    /**
     * 记录人
     */
    @ApiModelProperty(name="recorder",value = "记录人")
    private String recorder;









}
