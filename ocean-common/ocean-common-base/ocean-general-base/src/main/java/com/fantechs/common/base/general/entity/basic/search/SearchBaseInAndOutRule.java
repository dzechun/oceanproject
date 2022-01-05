package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/30
 */
@Data
public class SearchBaseInAndOutRule extends BaseQuery implements Serializable {
    private Long warehouseId;

    private String warehouseCode;

    private String warehouseName;

    private Byte category;
}
