package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesSfcBarcodeProcess extends BaseQuery implements Serializable {

    /**
     * 产品条码
     */
    private String barCode;

    /**
     * 包箱号
     */
    private String cartonCode;

    /**
     * 栈板号
     */
    private String palletCode;

    /**
     * 工单ID
     */
    private Long workOrderId;

    /**
     * 产品物料ID
     */
    private Long materialId;
}
