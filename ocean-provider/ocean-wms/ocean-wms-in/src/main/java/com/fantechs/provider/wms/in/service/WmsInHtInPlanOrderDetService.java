package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/08.
 */

public interface WmsInHtInPlanOrderDetService extends IService<WmsInHtInPlanOrderDet> {
    List<WmsInInPlanOrderDetDto> findList(Map<String, Object> map);
}
