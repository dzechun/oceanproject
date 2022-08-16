package com.fantechs.common.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = -1732327245940386967L;
    /**
     * 开始时间
     */
    @ApiModelProperty(name="startTime" ,value="开始时间(YYYY-MM-DD)")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="endTime" ,value="结束时间(YYYY-MM-DD)")
    private String endTime;

    /**
     * 当前页数
     */
    @ApiModelProperty(name="startPage" ,value="当前页")
    private Integer startPage=1;

    /**
     * 当前页数
     */
    @ApiModelProperty(name="pageSize" ,value="显示数量")
    private Integer pageSize=10;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;
}
