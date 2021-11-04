package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngMaterialMaintainLogDto;
import com.fantechs.common.base.general.entity.eng.EngMaterialMaintainLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/04.
 */

public interface EngMaterialMaintainLogService extends IService<EngMaterialMaintainLog> {
    List<EngMaterialMaintainLogDto> findList(Map<String, Object> map);
}
