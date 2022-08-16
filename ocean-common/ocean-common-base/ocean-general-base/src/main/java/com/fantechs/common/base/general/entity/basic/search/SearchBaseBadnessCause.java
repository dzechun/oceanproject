package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchBaseBadnessCause extends BaseQuery implements Serializable {

    /**
     * 不良原因代码
     */
    @ApiModelProperty(name="badnessCauseCode",value = "不良原因代码")
    private String badnessCauseCode;

    /**
     * 不良原因描述
     */
    @ApiModelProperty(name="badnessCauseDesc",value = "不良原因描述")
    private String badnessCauseDesc;

}
