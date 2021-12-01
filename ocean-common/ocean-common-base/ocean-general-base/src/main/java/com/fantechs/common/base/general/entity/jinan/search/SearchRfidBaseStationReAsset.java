package com.fantechs.common.base.general.entity.jinan.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchRfidBaseStationReAsset extends BaseQuery implements Serializable {

    /**
     * 基站ID
     */
    @ApiModelProperty(name="baseStationId",value = "基站ID")
    private Long baseStationId;
}
