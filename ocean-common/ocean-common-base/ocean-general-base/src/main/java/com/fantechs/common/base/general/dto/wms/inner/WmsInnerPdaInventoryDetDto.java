package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsInnerPdaInventoryDetDto  implements Serializable {
    /**
     * 库存明细ID
     */
    @ApiModelProperty(name="inventoryDetId",value = "库存明细ID")
    private Long inventoryDetId;

    /**
     * 作业单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    private Long jobOrderId;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseId",value = "仓库")
    private Long warehouseId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    private Long storageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    private BigDecimal materialQty;


    private static final long serialVersionUID = 1L;
}
