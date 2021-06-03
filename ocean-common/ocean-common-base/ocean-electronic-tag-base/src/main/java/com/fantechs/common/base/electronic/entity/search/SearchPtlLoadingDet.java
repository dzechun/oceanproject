package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlLoadingDet extends BaseQuery implements Serializable {

    /**
     * 上料单Id
     */
    @ApiModelProperty(name="loadingId",value = "上料单Id")
    private Long loadingId;

    /**
     * 上料单号
     */
    @ApiModelProperty(name="loadingCode",value = "上料单号")
    private String loadingCode;

    /**
     * 物料Id
     */
    @ApiModelProperty(name="materialId",value = "物料Id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 状态（0-未开始 1-上料中 2-部分完成 3-已完成）
     */
    @ApiModelProperty(name="status",value = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）")
    private Byte status;

    /**
     * 不等于该物料Id
     */
    @ApiModelProperty(name="notEqualMaterialId",value = "不等于该物料Id")
    private Long notEqualMaterialId;

    /**
     * 不等于该状态（0-未开始 1-上料中 2-部分完成 3-已完成）
     */
    @ApiModelProperty(name="statusList",value = "不等于该状态（0-未开始 1-上料中 2-部分完成 3-已完成）")
    private Byte notEqualstatus;
}
