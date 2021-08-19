package com.fantechs.provider.daq.service;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface DaqEquipmentService extends IService<DaqEquipment> {
    List<DaqEquipmentDto> findList(Map<String, Object> map);
    List<DaqHtEquipment> findHtList(Map<String, Object> map);

    int batchUpdate(List<DaqEquipment> list);

    DaqEquipment detailByIp(String ip);

    List<DaqEquipmentDto> findNoGroup(Map<String, Object> map);
}
