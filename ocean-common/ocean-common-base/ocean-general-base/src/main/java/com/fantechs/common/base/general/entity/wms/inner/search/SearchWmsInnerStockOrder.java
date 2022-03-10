package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
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

    /**
     * 盘点方式 1-PDA盘点 2-纸质盘点
     */
    @ApiModelProperty(name="stockMode",value = "盘点方式 1-PDA盘点 2-纸质盘点")
    private Byte stockMode;

    /**
     * 盲盘 1-是 2-否
     */
    @ApiModelProperty(name="blind",value = "盲盘 1-是 2-否")
    private String ifBlindStock;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;
}
