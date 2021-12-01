package com.fantechs.common.base.general.dto.jinan.Import;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RfidAreaImport implements Serializable {

    /**
     * 区域编码
     */
    @ApiModelProperty(name="areaCode" ,value="区域编码")
    @Excel(name = "区域编码(必填)", height = 20, width = 30)
    private String areaCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(name="areaName" ,value="区域名称")
    @Excel(name = "区域名称(必填)", height = 20, width = 30)
    private String areaName;

    /**
     * 区域描述
     */
    @ApiModelProperty(name="areaDesc" ,value="区域描述")
    @Excel(name = "区域描述", height = 20, width = 30)
    private String areaDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Integer status;
}
