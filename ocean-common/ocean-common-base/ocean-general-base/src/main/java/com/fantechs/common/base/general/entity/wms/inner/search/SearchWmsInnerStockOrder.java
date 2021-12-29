package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/5/27
 */
@Data
public class SearchWmsInnerStockOrder extends BaseQuery implements Serializable {

    @ApiModelProperty("盘点单ID")
    private Long stockOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    private String sysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    private Byte sourceBigType;

    @ApiModelProperty("盘点单号")
    private String planStockOrderCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    /**
     * 盘点类型(1-物料 2-库位 3-全盘)--
     */
    @ApiModelProperty(name="stockType",value = "盘点类型(1-物料 2-库位 3-全盘)")
    private Byte stockType;

    /**
     * 盘点或复盘(1-盘点 2-复盘)
     */
    @ApiModelProperty(name="projectType",value = "盘点或复盘(1-盘点 2-复盘)")
    private Byte projectType;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty("仓库名称")
    private String  warehouseName;

    /**
     * 单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-打开 2-待作业 3-作业中 4-待处理 5-完成 6-作废)")
    private Byte orderStatus;

    /**
     * 盘点方式(1-PDA盘点 2-纸质盘点)
     */
    @ApiModelProperty(name="stockMode",value = "盘点方式(1-PDA盘点 2-纸质盘点)")
    private Byte stockMode;

    /**
     * 是否盲盘(0-否 1-是)
     */
    @ApiModelProperty(name="ifBlindStock",value = "是否盲盘(0-否 1-是)")
    private Byte ifBlindStock;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    private boolean isPda;

}
