package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/27
 */
@Data
public class SearchWmsInnerStockOrder extends BaseQuery implements Serializable {
    @ApiModelProperty("盘点单号")
    private String stockOrderCode;
    @ApiModelProperty("仓库名称")
    private String  warehouseName;
    @ApiModelProperty("相关单号")
    private String relatedOrderCode;
    @ApiModelProperty("盘点类型")
    private Byte stockType;
    @ApiModelProperty("计划类型")
    private Byte projectType;
    @ApiModelProperty("状态")
    private Byte orderStatus;
    @ApiModelProperty("创建人")
    private String createUserName;
    private boolean isPda;

    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;
}
