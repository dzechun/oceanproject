package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WmsInnerPdaJobOrderDet implements Serializable {

    /**
     * 作业单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "作业单明细ID")
    @Excel(name = "作业单明细ID", height = 20, width = 30,orderNum="")
    private Long jobOrderDetId;

    /**
     * 移入库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "移入库位ID")
    private Long inStorageId;

    /**
     * 移出库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "移出库位ID")
    private Long outStorageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 拣货总数
     */
    @ApiModelProperty(name="actualQty",value = "拣货总数")
    private BigDecimal actualQty;

    /**
     * 扫描条码集合
     */
    @ApiModelProperty(name="inventoryDetList",value = "扫描条码集合")
    @Transient
    private List<WmsInnerPdaInventoryDetDto> inventoryDetList;

}
