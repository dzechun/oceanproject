package com.fantechs.common.base.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SmtProLineImport implements Serializable {

    /**
     * 产线编码
     */
    @ApiModelProperty(name="proCode" ,value="产线编码")
    @Excel(name = "产线编码(必填)", height = 20, width = 30)
    private String proCode;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName" ,value="产线名称")
    @Excel(name = "产线名称(必填)", height = 20, width = 30)
    private String proName;

    /**
     * 产线描述
     */
    @ApiModelProperty(name="proDesc" ,value="产线描述")
    @Excel(name = "产线描述", height = 20, width = 30)
    private String proDesc;

    /**
     * 厂别ID
     */
    @ApiModelProperty(name="factoryId" ,value="厂别ID")
    private Long factoryId;

    /**
     * 厂别编码
     */
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    @Excel(name = "厂别编码", height = 20, width = 30)
    private String factoryCode;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId" ,value="车间ID")
    private Long workShopId;

    /**
     * 车间编码
     */
    @ApiModelProperty(name="workShopCode" ,value="车间编码")
    @Excel(name = "车间编码", height = 20, width = 30)
    private String workShopCode;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    private Long organizationId;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="workShopCode" ,value="组织编码")
    @Excel(name = "组织编码", height = 20, width = 30)
    private String organizationCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 产线状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="产线状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Integer status;
}
