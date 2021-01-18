package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsBadItem extends BaseQuery implements Serializable {
    /**
     * 不良项目ID
     */
    @ApiModelProperty(name="badItemId",value = "不良项目ID")
    private Long badItemId;

    /**
     * 不良项目编码
     */
    @ApiModelProperty(name="badItemCode",value = "不良项目编码")
    private String badItemCode;

    /**
     * 不良原因
     */
    @ApiModelProperty(name="badCause",value = "不良原因")
    private String badCause;

    /**
     * 不良现象
     */
    @ApiModelProperty(name="badPhenomenon",value = "不良现象")
    private String badPhenomenon;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId",value = "工段ID")
    private Long sectionId;


    private static final long serialVersionUID = 1L;
}
