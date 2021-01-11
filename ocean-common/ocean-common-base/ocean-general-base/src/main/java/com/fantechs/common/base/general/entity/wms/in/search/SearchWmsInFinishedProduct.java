package com.fantechs.common.base.general.entity.wms.in.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SearchWmsInFinishedProduct extends BaseQuery implements Serializable {

    /**
     * 成品入库单号
     */
    @ApiModelProperty(name="finishedProductCode",value = "成品入库单号")
    private String finishedProductCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="operatorUserId",value = "处理人")
    private Long operatorUserId;

    /**
     * 处理人名称
     */
    @ApiModelProperty(name="operatorUserId",value = "处理人")
    private String operatorUserName;

}
