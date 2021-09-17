package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentBackupReEqu;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */

public interface EamHtEquipmentBackupReEquService extends IService<EamHtEquipmentBackupReEqu> {
    List<EamHtEquipmentBackupReEqu> findHtList(Map<String, Object> map);
}
