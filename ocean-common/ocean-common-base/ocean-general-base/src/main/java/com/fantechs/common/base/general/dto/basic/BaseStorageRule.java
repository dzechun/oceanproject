package com.fantechs.common.base.general.dto.basic;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2022/3/4
 */
@Data
public class BaseStorageRule implements Serializable {

    /**
     * erp逻辑仓
     */
    private Long LogicId;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 产线
     */
    private Long proLineId;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 销售编码
     */
    private String salesBarcode;

    /**
     * PO
     */
    private String poCode;

    private Long inventoryStatusId;

    /**
     * 工单数量
     */
    private BigDecimal workOrderQty;
}
