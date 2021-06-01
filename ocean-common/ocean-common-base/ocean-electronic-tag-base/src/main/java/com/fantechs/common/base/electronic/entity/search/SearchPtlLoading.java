package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlLoading extends BaseQuery implements Serializable {

    /**
     * 上料单号
     */
    @ApiModelProperty(name="loadingCode",value = "上料单号")
    private String loadingCode;

    /**
     * 来源系统名称
     */
    @ApiModelProperty(name="sourceSys",value = "来源系统名称")
    private String sourceSys;

    /**
     * 单据类型（1-采购单 2-入库单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-采购单 2-入库单）")
    private Byte orderType;

    /**
     * 状态（0-未开始 1-上料中 2-部分完成 3-已完成）
     */
    @ApiModelProperty(name="status",value = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）")
    private Byte status;
}
