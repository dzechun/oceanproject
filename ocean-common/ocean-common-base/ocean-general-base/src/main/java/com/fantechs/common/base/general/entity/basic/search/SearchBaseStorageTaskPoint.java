package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseStorageTaskPoint extends BaseQuery implements Serializable {

    /**
     * 库位配送点ID
     */
    @ApiModelProperty(name="storageTaskPointId",value = "库位配送点ID")
    private Long storageTaskPointId;

    /**
     * 配送点编码
     */
    @ApiModelProperty(name="taskPointCode",value = "配送点编码")
    private String taskPointCode;

    /**
     * 配送点名称
     */
    @ApiModelProperty(name="taskPointName",value = "配送点名称")
    private String taskPointName;

    /**
     * 配送点类型(1-备料 2-存料)
     */
    @ApiModelProperty(name="taskPointType",value = "配送点类型(1-备料 2-存料)")
    private Byte taskPointType;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 库区id
     */
    @ApiModelProperty(name="warehouseAreaId",value = "库区id")
    private Long warehouseAreaId;

    /**
     * 库区编码
     */
    @ApiModelProperty(name="warehouseAreaCode",value = "库区编码")
    private String warehouseAreaCode;

    /**
     * 库区名称
     */
    @ApiModelProperty(name="warehouseAreaName",value = "库区名称")
    private String warehouseAreaName;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 库位配送点状态(1-空闲 2-使用)
     */
    @ApiModelProperty(name="storageTaskPointStatus",value = "库位配送点状态(1-空闲 2-使用)")
    private Byte storageTaskPointStatus;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 是否按优先级排序(设1为按优先级排序)
     */
    @ApiModelProperty(name = "ifOrderByUsePriority",value = "是否按优先级排序(设1为按优先级排序)")
    private Integer ifOrderByUsePriority;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 坐标编码
     */
    @ApiModelProperty(name="xyzCode",value = "坐标编码")
    private String xyzCode;

    /**
     * 优先级
     */
    @ApiModelProperty(name="usePriority",value = "优先级")
    private Integer usePriority;

    /**
     * 层级类别
     */
    @ApiModelProperty(name="hierarchicalCategory",value = "层级类别")
    private String hierarchicalCategory;
}
