package com.fantechs.common.base.general.entity.eam.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchEamWiReleaseDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="wiReleaseId",value = "ESOP发布管理ID")
    private Long wiReleaseId;
}
