package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wcz on 2020/10/12.
 */
@Data
public class SearchSmtRoute extends BaseQuery implements Serializable {

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
}
