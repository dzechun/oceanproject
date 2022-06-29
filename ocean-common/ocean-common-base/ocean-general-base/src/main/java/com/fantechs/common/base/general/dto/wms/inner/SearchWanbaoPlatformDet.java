package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.dto.BaseQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2022/6/29
 */
@Data
public class SearchWanbaoPlatformDet extends BaseQuery implements Serializable {
    private Long platformId;
}
