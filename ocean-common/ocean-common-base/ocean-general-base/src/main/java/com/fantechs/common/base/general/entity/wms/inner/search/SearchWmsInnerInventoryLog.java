package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Data
public class SearchWmsInnerInventoryLog extends BaseQuery implements Serializable {
    /**
     *仓库
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    private String warehouseName;

    /**
     *库位
     */
    @ApiModelProperty(name = "storageCode",value = "库位")
    private String storageCode;

    /**
     *相关单号
     */
    @ApiModelProperty(name = "relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    private String supplierName;

    /**
     * 作业类型(1-收货，2-上架，3-移位，4-拣货，5-补货，6-调整，7-盘点，8-发运)
     */
    @ApiModelProperty(name="jobOrderType",value = "作业类型(1-收货，2-上架，3-移位，4-拣货，5-补货，6-调整，7-盘点，8-发运)")
    private Byte jobOrderType;

    /**
     * 库存状态名称
     */
    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    private String inventoryStatusName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 加减类型(1-加 2-减)
     */
    @ApiModelProperty(name="addOrSubtract",value = "加减类型(1-加 2-减)")
    private Byte addOrSubtract;

    /**
     * 生产日期开始
     */
    @ApiModelProperty(name="productionDateT",value = "生产日期开始")
    private Date productionDateT;
    /**
     * 生产日期结束
     */
    @ApiModelProperty(name="productionDateF",value = "生产日期结束")
    private Date productionDateF;

    /**
     * 过期日期开始
     */
    @ApiModelProperty(name="expiredDateT",value = "过期日期开始")
    private Date expiredDateT;
    /**
     * 过期日期结束
     */
    @ApiModelProperty(name="expiredDateF",value = "过期日期结束")
    private Date expiredDateF;

    /**
     * 质检日期开始
     */
    @ApiModelProperty(name="qcDateT",value = "质检日期开始")
    private Date qcDateT;
    /**
     * 质检日期结束
     */
    @ApiModelProperty(name="qcDateF",value = "质检日期结束")
    private Date qcDateF;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    private String batchCode;

    /**
     * 收货单号
     */
    @ApiModelProperty(name="asnCode",value = "收货单号")
    private String asnCode;

    /**
     * 发货单号
     */
    @ApiModelProperty(name="despatchOrderCode",value = "发货单号")
    private String despatchOrderCode;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    private String palletCode;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    private String modifiedUserName;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    private String packingUnitName;

    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;
}
