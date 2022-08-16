package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseStaffProcess extends BaseQuery implements Serializable {

    /**
     * 员工id
     */
    @ApiModelProperty(name="staffId",value = "员工id")
    private Long staffId;

    /**
     * 工种id
     */
    @ApiModelProperty(name="processId",value = "工种id")
    private Long processId;

}
