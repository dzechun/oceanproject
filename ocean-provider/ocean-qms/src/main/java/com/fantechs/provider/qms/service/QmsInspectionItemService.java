package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */

public interface QmsInspectionItemService extends IService<QmsInspectionItem> {

    List<QmsInspectionItemDto> findList(Map<String, Object> map);
}
