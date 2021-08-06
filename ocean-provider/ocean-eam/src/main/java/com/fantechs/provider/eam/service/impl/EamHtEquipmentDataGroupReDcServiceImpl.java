package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroupReDc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamHtEquipmentDataGroupReDcMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentDataGroupReDcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class EamHtEquipmentDataGroupReDcServiceImpl extends BaseService<EamHtEquipmentDataGroupReDc> implements EamHtEquipmentDataGroupReDcService {

    @Resource
    private EamHtEquipmentDataGroupReDcMapper eamHtEquipmentDataGroupReDcMapper;

    @Override
    public List<EamHtEquipmentDataGroupReDcDto> findHtList(Map<String, Object> map) {
        return eamHtEquipmentDataGroupReDcMapper.findList(map);
    }
}
