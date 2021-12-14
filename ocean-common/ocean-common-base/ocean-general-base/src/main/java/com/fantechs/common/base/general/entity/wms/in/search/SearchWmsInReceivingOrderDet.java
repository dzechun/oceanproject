package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/12/14
 */
@Data
public class SearchWmsInReceivingOrderDet extends BaseQuery implements Serializable {

    private Long receivingOrderId;

    private List<Byte> lineStatusList;
}
