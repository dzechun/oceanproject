package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseStorageTaskPointImport implements Serializable {

    /**
     * 配送点编码(必填)
     */
    @ApiModelProperty(name = "taskPointCode",value = "配送点编码")
    @Excel(name = "配送点编码(必填)", height = 20, width = 30)
    private String taskPointCode;

    /**
     * 配送点名称(必填)
     */
    @ApiModelProperty(name = "taskPointName",value = "配送点名称(必填)")
    @Excel(name = "配送点名称(必填)", height = 20, width = 30)
    private String taskPointName;

    /**
     * 配送点类型(1-备料 2-存料)
     */
    @ApiModelProperty(name = "taskPointType",value = "配送点类型(1-备料 2-存料)")
    @Excel(name = "配送点类型(1-备料 2-存料)", height = 20, width = 30)
    private Integer taskPointType;

    /**
     * 库位编码(必填)
     */
    @ApiModelProperty(name = "storageCode",value = "库位编码(必填)")
    @Excel(name = "库位编码(必填)", height = 20, width = 30)
    private String storageCode;

    /**
     * 库位id
     */
    @ApiModelProperty(name = "storageId",value = "库位id")
    private Long storageId;

    /**
     * 坐标编码(必填)
     */
    @ApiModelProperty(name = "xyzCode",value = "坐标编码(必填)")
    @Excel(name = "坐标编码(必填)", height = 20, width = 30)
    private String xyzCode;

    /**
     * 使用优先级(必填)
     */
    @ApiModelProperty(name = "usePriority",value = "使用优先级(必填)")
    @Excel(name = "使用优先级(必填)", height = 20, width = 30)
    private Integer usePriority;

    /**
     * 配送方式
     */
    @ApiModelProperty(name="type",value = "配送方式")
    @Excel(name = "配送方式", height = 20, width = 30,orderNum="")
    private String type;

    /**
     * 库位配送点状态(1-空闲 2-使用)
     */
    @ApiModelProperty(name = "storageTaskPointStatus",value = "库位配送点状态(1-空闲 2-使用)")
    @Excel(name = "库位配送点状态(1-空闲 2-使用)", height = 20, width = 30)
    private Integer storageTaskPointStatus;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
