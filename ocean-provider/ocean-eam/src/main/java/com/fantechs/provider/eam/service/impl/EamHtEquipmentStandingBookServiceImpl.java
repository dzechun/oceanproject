package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentStandingBookDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBook;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamHtEquipmentStandingBookMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentStandingBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamHtEquipmentStandingBookServiceImpl extends BaseService<EamHtEquipmentStandingBook> implements EamHtEquipmentStandingBookService {

    @Resource
    private EamHtEquipmentStandingBookMapper eamHtEquipmentStandingBookMapper;

    @Override
    public List<EamHtEquipmentStandingBookDto> findHtList(Map<String, Object> map) {
        return eamHtEquipmentStandingBookMapper.findHtList(map);
    }
}
