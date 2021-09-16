package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentBackupReEquDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackupReEqu;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */

public interface EamEquipmentBackupReEquService extends IService<EamEquipmentBackupReEqu> {
    List<EamEquipmentBackupReEquDto> findList(Map<String, Object> map);
}
