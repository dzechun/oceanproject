package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class SearchWmsInnerInitStockDet extends BaseQuery implements Serializable {
    private Long initStockId;
}
