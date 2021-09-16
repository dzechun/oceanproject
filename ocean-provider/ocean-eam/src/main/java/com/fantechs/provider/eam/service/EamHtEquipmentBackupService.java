package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentBackup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */

public interface EamHtEquipmentBackupService extends IService<EamHtEquipmentBackup> {
    List<EamHtEquipmentBackup> findHtList(Map<String, Object> map);
}
