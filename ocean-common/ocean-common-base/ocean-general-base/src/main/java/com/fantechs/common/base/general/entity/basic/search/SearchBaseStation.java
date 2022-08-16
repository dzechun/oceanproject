package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseStation extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 5574891222916070856L;

    /**
     * 工位代码
     */
    @ApiModelProperty(name = "stationCode",value = "工位代码")
    private String stationCode;

    /**
     * 工位名称
     */
    @ApiModelProperty(name = "stationName",value = "工位名称")
    private String stationName;

    /**
     * 工位描述
     */
    @ApiModelProperty(name = "stationDesc",value = "工位描述")
    private String stationDesc;

    /**
     * 工序ID
     */
    @ApiModelProperty(name = "processId",value = "工序ID")
    private Long processId;

    /**
     * 工段ID
     */
    @ApiModelProperty(name = "sectionId",value = "工段ID")
    private Long sectionId;

}
