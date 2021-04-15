package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchBaseBadnessCause extends BaseQuery implements Serializable {

    /**
     * 不良类别代码
     */
    @ApiModelProperty(name="badnessCategoryCode",value = "不良类别代码")
    private String badnessCategoryCode;

    /**
     * 不良类别描述
     */
    @ApiModelProperty(name="badnessCategoryDesc",value = "不良类别描述")
    private String badnessCategoryDesc;

}
