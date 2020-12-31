package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtherout;
import com.fantechs.common.base.support.IService;

;import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */

public interface WmsOutOtheroutService extends IService<WmsOutOtherout> {

    List<WmsOutOtheroutDto> findList(Map<String, Object> map);

    List<WmsOutHtOtherout> findHTList(Map<String, Object> dynamicConditionByEntity);
}
