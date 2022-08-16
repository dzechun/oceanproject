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
     * 不良类型编码
     */
    @ApiModelProperty(name="badTypeCode",value = "不良类型编码")
    private String badTypeCode;

    /**
     * 不良类型原因
     */
    @ApiModelProperty(name="badTypeCause",value = "不良类型原因")
    private String badTypeCause;

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
