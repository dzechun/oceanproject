package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentBackupDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackup;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */

public interface EamEquipmentBackupService extends IService<EamEquipmentBackup> {
    List<EamEquipmentBackupDto> findList(Map<String, Object> map);
}
