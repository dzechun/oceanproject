package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderMaterialReP;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface MesPmHtWorkOrderMaterialRePService extends IService<MesPmHtWorkOrderMaterialReP> {
    List<MesPmHtWorkOrderMaterialReP> findList(Map<String, Object> map);
}
