package com.fantechs.common.base.general.entity.qms.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsFirstInspection extends BaseQuery implements Serializable {

    /**
     * 生产工单号
     */
    @ApiModelProperty(name = "workOrderCode",value = "生产工单号")
    private String workOrderCode;

    /**
     * 首检单号
     */
    @ApiModelProperty(name="firstInspectionCode",value = "首检单号")
    private String firstInspectionCode;

    /**
     * 生产线
     */
    @ApiModelProperty(name="productionLine",value = "生产线")
    private String  productionLine;

    /**
     * 处理人
     */
    @ApiModelProperty(name="handler",value = "处理人名称")
    private String handler;

    private static final long serialVersionUID = 1L;
}
