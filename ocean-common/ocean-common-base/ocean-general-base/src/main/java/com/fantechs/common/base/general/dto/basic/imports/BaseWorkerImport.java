package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseWorkerImport implements Serializable {

    /**
     * 用户编码
     */
    @ApiModelProperty(name="userCode",value = "用户编码")
    @Excel(name = "用户编码(必填)", height = 20, width = 30)
    private String userCode;

    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId",value = "用户ID")
    private Long userId;

    /**
     * 所属仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "所属仓库编码")
    @Excel(name = "所属仓库编码", height = 20, width = 30)
    private String warehouseCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 工作区编码(必填)
     */
    @ApiModelProperty(name = "workingAreaCode",value = "工作区编码(必填)")
    @Excel(name = "工作区编码(必填)", height = 20, width = 30)
    private String workingAreaCode;

    /**
     * 工作区ID
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区ID")
    private Long workingAreaId;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
