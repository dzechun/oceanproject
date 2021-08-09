package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupReDc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamEquipmentDataGroupReDcMapper;
import com.fantechs.provider.eam.service.EamEquipmentDataGroupReDcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class EamEquipmentDataGroupReDcServiceImpl extends BaseService<EamEquipmentDataGroupReDc> implements EamEquipmentDataGroupReDcService {

    @Resource
    private EamEquipmentDataGroupReDcMapper eamEquipmentDataGroupReDcMapper;

    @Override
    public List<EamEquipmentDataGroupReDcDto> findList(Map<String, Object> map) {
        return eamEquipmentDataGroupReDcMapper.findList(map);
    }
}
