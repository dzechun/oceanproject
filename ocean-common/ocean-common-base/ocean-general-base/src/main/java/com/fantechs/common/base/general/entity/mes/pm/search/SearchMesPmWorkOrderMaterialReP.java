package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesPmWorkOrderMaterialReP extends BaseQuery implements Serializable {


    /**
     * 物料工序关系表ID
     */
    @ApiModelProperty(name="workOrderProcessReWoId",value = "物料工序关系表ID")
    private Long workOrderProcessReWoId;

    /**
     * 扫描类别(1-物料 2-条码)
     */
    @ApiModelProperty(name="scanType",value = "扫描类别(1-物料 2-条码)")
    private Byte scanType;

    private static final long serialVersionUID = 1L;
}
