package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseWorkShopImport implements Serializable {

    /**
     * 车间编码
     */
    @Excel(name = "车间编码(必填)", height = 20, width = 30)
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Excel(name = "车间名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 车间描述
     */
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    @Excel(name = "车间描述", height = 20, width = 30)
    private String workShopDesc;

    /**
     * 工厂id
     */
    @ApiModelProperty(name = "factoryId",value = "工厂id")
    private Long factoryId;

    /**
     * 工厂编码
     */
    @ApiModelProperty(name = "factoryName",value = "工厂编码")
    @Excel(name = "工厂编码(必传)", height = 20, width = 30)
    private String factoryCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "工厂编码", height = 20, width = 30)
    private String remark;

    /**
     * 车间状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name = "status",value = "车间状态（0、不启用 1、启用）")
    @Excel(name = "车间状态", height = 20, width = 30)
    private Integer status;
}
