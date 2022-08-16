package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/21
 */
@Data
public class SearchOmOtherInOrderDet extends BaseQuery implements Serializable {
    private Long otherInOrderId;
}
