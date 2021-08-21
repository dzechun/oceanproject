package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJig;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamHtEquipmentJigService extends IService<EamHtEquipmentJig> {
    List<EamHtEquipmentJig> findList(Map<String, Object> map);
}
