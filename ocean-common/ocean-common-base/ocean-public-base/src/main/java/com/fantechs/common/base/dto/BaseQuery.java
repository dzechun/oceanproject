package com.fantechs.common.base.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = -1732327245940386967L;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="endTime" ,value="结束时间(YYYY-MM-DD)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

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

    /**
     * 高级查询
     */
    @ApiModelProperty(name="map",value = "高级查询")
    private Map<String,Query> query;

    /**
     * 查询对象
     */
    @ApiModelProperty(name="map",value = "查询对象")
    private Query q;
}
