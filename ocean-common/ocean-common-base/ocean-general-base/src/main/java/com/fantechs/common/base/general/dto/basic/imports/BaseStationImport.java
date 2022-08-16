package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseStationImport implements Serializable {

    /**
     * 工位代码
     */
    @ApiModelProperty(name = "stationCode",value = "工位代码")
    @Excel(name = "工位代码(必填)", height = 20, width = 30)
    private String stationCode;

    /**
     * 工位名称
     */
    @ApiModelProperty(name = "stationName",value = "工位名称")
    @Excel(name = "工位名称(必填)", height = 20, width = 30)
    private String stationName;

    /**
     * 工位描述
     */
    @ApiModelProperty(name = "stationDesc",value = "工位描述")
    @Excel(name = "工位描述", height = 20, width = 30)
    private String stationDesc;

    /**
     * 工序ID
     */
    @ApiModelProperty(name = "processId",value = "工序ID")
    private Long processId;

    /**
     * 工序编码
     */
    @ApiModelProperty(name = "processCode",value = "工序编码")
    @Excel(name = "工序编码(必填)", height = 20, width = 30)
    private String processCode;

    /**
     * 工段ID
     */
    @ApiModelProperty(name = "sectionId",value = "工段ID")
    private Long sectionId;

    /**
     * 工段编码
     */
    @ApiModelProperty(name = "sectionCode",value = "工段编码")
    @Excel(name = "工段编码(必填)", height = 20, width = 30)
    private String sectionCode;

    /**
     * 是否过站(0.否  1.是)
     */
    @ApiModelProperty(name = "ifPassStation",value = "是否过站")
    @Excel(name = "是否过站(0.否  1.是)", height = 20, width = 30)
    private Integer ifPassStation;

    /**
     * 备注
     */
    @Excel(name = "备注", height = 20, width = 30)
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Integer status;
}
