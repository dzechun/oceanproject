package com.fantechs.common.base.general.entity.srm.search;

import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchSrmCarportTimeQuantum extends BaseQuery implements Serializable {

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 开始时间
     */
    @ApiModelProperty(name="carportStartTime",value = "开始时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date carportStartTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="carportEndTime",value = "结束时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date carportEndTime;

    private static final long serialVersionUID = 1L;
}
