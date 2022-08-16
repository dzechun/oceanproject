package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseLabelMaterialImport implements Serializable {

    /**
     * 物料编码(必填)
     */
    @ApiModelProperty(name="materialCode",value = "物料编码(必填)")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 标签编码(必填)
     */
    @ApiModelProperty(name="labelCode",value = "标签编码(必填)")
    @Excel(name = "标签编码(必填)", height = 20, width = 30)
    private String labelCode;

    /**
     * 标签ID
     */
    @ApiModelProperty(name="labelId",value = "标签ID")
    private Long labelId;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30)
    private String processCode;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 打印张数
     */
    @ApiModelProperty(name = "oncePrintCount",value = "打印张数")
    @Excel(name = "打印张数", height = 20, width = 30)
    private Integer oncePrintCount;

    /**
     * 是否工序打印（0-否 1-是）
     */
    @ApiModelProperty(name = "isProcess",value = "是否工序打印（0-否 1-是）")
    @Excel(name = "是否工序打印（0-否 1-是）", height = 20, width = 30)
    private Integer isProcess;
}
