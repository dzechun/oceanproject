package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class SearchWmsInnerInitStock extends BaseQuery implements Serializable {

    private Long warehouseId;

    private Long storagrId;

    private String storageCode;

    private String warehouseName;

    private byte initStockType;

    private String initStockOrderCode;

    private byte orderStatus;

    private List<Byte> orderStatusList;
}
