package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 流程单工序退回表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesPmProcessListProcessReListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "工单流程卡任务池ID",example = "工单流程卡任务池ID")
    private Long workOrderCardPoolId;
    @ApiModelProperty(value = "操作退回工序ID",example = "操作退回工序ID")
    private Long processId;
    @ApiModelProperty(value = "指定退回工序ID",example = "指定退回工序ID")
    private Long reProcessId;
    @ApiModelProperty(value = "产线ID",example = "产线ID")
    private Long proLineId;
    @ApiModelProperty(value = "流程单工序退回编码",example = "流程单工序退回编码")
    private String processListProcessReCode;
    @ApiModelProperty(value = "工单编号",example = "工单编号")
    private String workOrderCode;
    @ApiModelProperty(value = "产品编号",example = "产品编号")
    private String materialCode;
}
