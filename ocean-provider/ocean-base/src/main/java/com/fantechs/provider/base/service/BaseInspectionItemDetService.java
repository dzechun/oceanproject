package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItemDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */

public interface BaseInspectionItemDetService extends IService<BaseInspectionItemDet> {

    List<BaseInspectionItemDetDto> findList(Map<String, Object> map);
}
