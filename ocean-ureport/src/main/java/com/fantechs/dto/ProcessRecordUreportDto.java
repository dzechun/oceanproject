package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
public class ProcessRecordUreportDto implements Serializable {

    @ApiModelProperty(name="proName",value = "产线名称")
    @Excel(name = "产线", height = 20, width = 30)
    private String proName;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    @ApiModelProperty(name="userName",value = "操作人")
    @Excel(name = "操作人", height = 20, width = 30)
    private String userName;

    @ApiModelProperty(name = "processName",value = "工序")
    @Excel(name = "工序", height = 20, width = 30)
    private String processName;

    @ApiModelProperty(name="finishDate",value = "统计日期")
    @Excel(name = "统计日期", height = 20, width = 30)
    private String finishDate;

    @ApiModelProperty(name="finishQty",value = "完成数量")
    @Excel(name = "完成数量", height = 20, width = 30)
    private String finishQty;
}
