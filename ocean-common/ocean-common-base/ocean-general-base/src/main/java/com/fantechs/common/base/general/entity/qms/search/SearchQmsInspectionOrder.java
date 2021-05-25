package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsInspectionOrder extends BaseQuery implements Serializable {

    /**
     * 检验单号
     */
    @ApiModelProperty(name="inspectionOrderCode",value = "检验单号")
    private String inspectionOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    private Byte inspectionStatus;

    /**
     * 检验结果(0-不合格 1-合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-不合格 1-合格)")
    private Byte inspectionResult;

}
