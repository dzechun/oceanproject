package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2022/6/27
 */
@Data
public class SearchWanbaoPlatform extends BaseQuery implements Serializable {
    /**
     * 使用状态(1-空闲 2-使用中)
     */
    @ApiModelProperty(name="usageStatus",value = "使用状态(1-空闲 2-使用中)")
    private Byte usageStatus;
}
