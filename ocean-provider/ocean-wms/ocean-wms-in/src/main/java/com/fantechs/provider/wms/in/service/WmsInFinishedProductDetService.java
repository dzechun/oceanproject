package com.fantechs.provider.wms.in.service;


import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface WmsInFinishedProductDetService extends IService<WmsInFinishedProductDet> {

    List<WmsInFinishedProductDetDto> findList(Map<String, Object> dynamicConditionByEntity);
}
