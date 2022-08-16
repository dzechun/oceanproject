package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEmpWorkload extends BaseQuery implements Serializable {

    /**
     * 员工号
     */
    @ApiModelProperty(name = "empNumber", value = "员工号")
    private String empNumber;

    /**
     * 姓名
     */
    @ApiModelProperty(name = "name", value = "姓名")
    private String name;


}
