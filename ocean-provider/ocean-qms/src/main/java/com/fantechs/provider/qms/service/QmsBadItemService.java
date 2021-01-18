package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/16.
 */

public interface QmsBadItemService extends IService<QmsBadItem> {
    List<QmsBadItemDto> findList(Map<String, Object> map);
}
