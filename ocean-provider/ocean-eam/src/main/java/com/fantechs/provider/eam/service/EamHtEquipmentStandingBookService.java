package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentStandingBookDto;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBook;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamHtEquipmentStandingBookService extends IService<EamHtEquipmentStandingBook> {
    List<EamHtEquipmentStandingBookDto> findHtList(Map<String,Object> map);
}
