package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtProcess extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -6658723130549341427L;

    /**
     * 工序代码
     */
    @ApiModelProperty(name="processCode" ,value="工序代码")
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 工序描述
     */
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    private String processDesc;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    private Long sectionId;

    /**
     * 工序类别ID
     */
    @ApiModelProperty(name = "processCategoryId",value = "工序类别id")
    private Long processCategoryId;
}
