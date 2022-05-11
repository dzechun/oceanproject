package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchProcessRecordUreportDto extends BaseQuery implements Serializable {

    @ApiModelProperty(name="proName",value = "产线名称")
    private String proName;

    @ApiModelProperty(name="userName",value = "操作人")
    private String userName;

    @ApiModelProperty(name = "processName",value = "工序")
    private String processName;

    @ApiModelProperty(name="finishDate",value = "统计日期")
    private String finishDate;
}
