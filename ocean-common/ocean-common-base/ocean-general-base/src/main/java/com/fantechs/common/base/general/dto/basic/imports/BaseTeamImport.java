package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseTeamImport implements Serializable {

    /**
     * 班组代码
     */
    @ApiModelProperty(name="teamCode",value = "班组代码")
    @Excel(name = "班组代码(必填)", height = 20, width = 30)
    private String teamCode;

    /**
     * 班组名称
     */
    @ApiModelProperty(name="teamName",value = "班组名称")
    @Excel(name = "班组名称(必填)", height = 20, width = 30)
    private String teamName;

    /**
     * 班组描述
     */
    @ApiModelProperty(name="teamDesc",value = "班组描述")
    @Excel(name = "班组描述", height = 20, width = 30)
    private String teamDesc;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    private Long workShopId;

    /**
     * 车间编码
     */
    @Excel(name = "车间编码", height = 20, width = 30)
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
