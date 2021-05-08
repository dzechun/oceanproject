package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;

@Data
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
}
