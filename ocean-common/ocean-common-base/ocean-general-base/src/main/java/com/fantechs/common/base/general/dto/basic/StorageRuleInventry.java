package com.fantechs.common.base.general.dto.basic;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2022/3/4
 */
@Data
public class StorageRuleInventry implements Serializable {
    private Long storageId;

    private Long materialId;

    private String salesBarcode;

    private String poCode;

    private BigDecimal materialQty;
}
