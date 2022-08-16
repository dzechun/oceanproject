package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface MesPmWorkOrderMaterialRePService extends IService<MesPmWorkOrderMaterialReP> {
    List<MesPmWorkOrderMaterialRePDto> findList(Map<String, Object> map);
}
