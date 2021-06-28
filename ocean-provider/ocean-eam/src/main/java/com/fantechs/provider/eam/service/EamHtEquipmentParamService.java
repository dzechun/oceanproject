package com.fantechs.provider.eam.service;


import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamHtEquipmentParamService extends IService<EamHtEquipmentParam> {
    List<EamHtEquipmentParam> findHtList(Map<String, Object> map);
}
