package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipmentDataGroupReDc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentDataGroupReDcMapper;
import com.fantechs.provider.daq.service.DaqHtEquipmentDataGroupReDcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class DaqHtEquipmentDataGroupReDcServiceImpl extends BaseService<DaqHtEquipmentDataGroupReDc> implements DaqHtEquipmentDataGroupReDcService {

    @Resource
    private DaqHtEquipmentDataGroupReDcMapper daqHtEquipmentDataGroupReDcMapper;

    @Override
    public List<DaqHtEquipmentDataGroupReDcDto> findHtList(Map<String, Object> map) {
        return daqHtEquipmentDataGroupReDcMapper.findList(map);
    }
}
