package com.fantechs.common.base.general.dto.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchMesPmMatchingOrder extends BaseQuery implements Serializable {

    /**
     * 配套单号
     */
    @ApiModelProperty(name="matchingOrderCode",value = "配套单号")
    @Excel(name = "配套单号", height = 20, width = 30)
    @Column(name = "matching_order_code")
    private String matchingOrderCode;

    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    private String workOrderCardId;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

}
