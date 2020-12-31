package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtheroutDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */

public interface WmsOutOtheroutDetService extends IService<WmsOutOtheroutDet> {

    List<WmsOutOtheroutDetDto> findList(Map<String, Object> map);

    //批量更新
    int batchUpdate(List<WmsOutOtheroutDet> wmsOutOtheroutDets);

    List<WmsOutHtOtheroutDet> findHTList(Map<String, Object> dynamicConditionByEntity);
}
