package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/11/23
 */
@Data
public class SearchSmtProcessListProcess extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 1523635375536523661L;
    @ApiModelProperty(value = "工单流程卡任务池ID",example = "工单流程卡任务池ID")
    private Long workOrderCardPoolId;
    @ApiModelProperty(value = "工序ID",example = "工序ID")
    private Long processId;
    @ApiModelProperty(value = "产线ID",example = "产线ID")
    private Long proLineId;
    @ApiModelProperty(value = "流程单工序编码",example = "流程单工序编码")
    private String processListProcessCode;
    @ApiModelProperty(value = "工单编号",example = "工单编号")
    private String workOrderCode;
    @ApiModelProperty(value = "产品编号",example = "产品编号")
    private String materialCode;
}