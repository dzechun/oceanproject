package com.fantechs.common.base.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class Query implements Serializable {
    /**
     * 排序
     */
    @ApiModelProperty(name="querySeq",value = "排序（desc、asc）")
    private String querySeq;

    /**
     * 查询方式
     */
    @ApiModelProperty(name="queryType",value = "查询方式（=、>、<、=<、=>、in(包含)、not in(不包含)、like(模糊)、between(范围)）")
    private String queryType;

    /**
     * 连接查询
     */
    @ApiModelProperty(name="joinQuery",value = "连接查询（and(并且)、or(或者)）")
    private String joinQuery;

    /**
     * 查询值
     */
    @ApiModelProperty(name="value",value = "查询值")
    private String value;

    /**
     * sql
     */
    @ApiModelProperty(name="sql",value = "sql")
    private String sql;
}
