package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchSmtPaddingMaterial extends BaseQuery implements Serializable {

    /**
     * 上料单号
     */
    @ApiModelProperty(name="paddingMaterialCode",value = "上料单号")
    private String paddingMaterialCode;

    /**
     * 工单号(预留)
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号(预留)")
    private String workOrderCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;


    /**
     * 状态(0-未开始，1-分拣中 2-已完成)
     */
    @ApiModelProperty(name="status",value = "状态(0-未开始，1-分拣中 2-已完成)")
    private Byte status;

    /**
     * 上料单Id
     */
    @ApiModelProperty(name="paddingMaterialId",value = "上料单Id")
    private Long paddingMaterialId;
}
