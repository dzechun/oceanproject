package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherin;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/12.
 */

public interface WmsInOtherinService extends IService<WmsInOtherin> {

    List<WmsInOtherinDto> findList(Map<String, Object> dynamicConditionByEntity);

    List<WmsInOtherinDto> findHtList(Map<String, Object> dynamicConditionByEntity);
}
