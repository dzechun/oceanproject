package com.fantechs.common.base.general.entity.leisai.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchLeisaiProcessInputOrder extends BaseQuery implements Serializable {

    /**
     * 单据号
     */
    @ApiModelProperty(name="processInputOrderCode",value = "单据号")
    private String processInputOrderCode;

    /**
     * 生产订单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产订单")
    private String workOrderCode;

    /**
     * 工序号
     */
    @ApiModelProperty(name="processCode",value = "工序号")
    private String processCode;

    /**
     * 记录人
     */
    @ApiModelProperty(name="recorder",value = "记录人")
    private String recorder;









}
