package com.fantechs.common.base.general.entity.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEsopWiFile extends BaseQuery implements Serializable {

    /**
     * 是否可分配(0-可分配,1-不能分配)
     */
    @ApiModelProperty(name="isDistribut",value = "是否可分配(0-可分配,1-不能分配)")
    private Byte isDistribut;

    /**
     * 文件名
     */
    @ApiModelProperty(name="wiFileName",value = "文件名")
    private String wiFileName;
}
