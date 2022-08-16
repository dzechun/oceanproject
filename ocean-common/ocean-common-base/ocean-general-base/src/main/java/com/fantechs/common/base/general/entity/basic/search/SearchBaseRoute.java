package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by wcz on 2020/10/12.
 */
@Data
public class SearchBaseRoute extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -5629193415229623395L;
    /**
     * 工艺路线代码
     */
    @ApiModelProperty(name="routeCode" ,value="工艺路线代码")
    private String routeCode;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 工艺路线描述
     */
    @ApiModelProperty(name="routeDesc" ,value="工艺路线描述")
    private String routeDesc;

    /**
     * 工艺路线类型（1、成品 2、半成品 3、部件）
     */
    @ApiModelProperty(name="routeType" ,value="工艺路线类型")
    private Integer routeType;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 名称查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "nameQueryMark",value = "名称查询标记(设为1做等值查询)")
    private Integer nameQueryMark;
}
