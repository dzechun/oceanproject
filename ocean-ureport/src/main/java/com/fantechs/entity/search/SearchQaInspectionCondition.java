package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQaInspectionCondition extends BaseQuery implements Serializable {

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode", value = "合同号")
    private String contractCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name = "supplierName", value = "客户名称")
    private String supplierName;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "workOrderCode", value = "工单号")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialCode", value = "产品料号")
    private String materialCode;

    /**
     * 抽检情况
     */
    @ApiModelProperty(name = "inspectionResult", value = "抽检情况")
    private String inspectionResult;

    /**
     * 检验人
     */
    @ApiModelProperty(name = "surveyor", value = "检验人")
    private String surveyor;

}
