package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */

public interface BaseInspectionItemService extends IService<BaseInspectionItem> {

    List<BaseInspectionItemDto> findList(Map<String, Object> map);

    List<BaseInspectionItemDto> exportExcel(Map<String, Object> map);
}
