package com.fantechs.service.impl;

import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.entity.EAMEquipmentBorad;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.mapper.EAMEquipmentBoradMapper;
import com.fantechs.service.EAMEquipmentBoradService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EAMEquipmentBoradServiceImpl implements EAMEquipmentBoradService {

    @Resource
    private EAMEquipmentBoradMapper eamEquipmentBoradMapper;

    @Override
    public EAMEquipmentBorad findList(SearchProLineBoard searchProLineBoard) {

       List<EamEquipment> eamEquipmentList = eamEquipmentBoradMapper.findEquipmentByLine(searchProLineBoard);








        return null;
    }
}
