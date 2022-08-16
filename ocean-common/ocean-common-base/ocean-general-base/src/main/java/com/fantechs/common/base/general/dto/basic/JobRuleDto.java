package com.fantechs.common.base.general.dto.basic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/8/12
 */
@Data
public class JobRuleDto {
    private BigDecimal packageQty;
    private Long warehouseId;
    private Long materialId;
    private String batchCode;
    private String proDate;
}
