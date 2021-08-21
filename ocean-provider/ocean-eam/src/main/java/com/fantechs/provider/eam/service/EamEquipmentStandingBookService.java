package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentStandingBook;
import com.fantechs.common.base.support.IService;
import java.util.List;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquipmentStandingBookService extends IService<EamEquipmentStandingBook> {

    List<EamEquipmentStandingBookDto> findList(SearchEamEquipmentStandingBook searchEamEquipmentStandingBook);

    int save(EamEquipmentStandingBookDto eamEquipmentStandingBookDto);

    int update(EamEquipmentStandingBookDto eamEquipmentStandingBookDto);
}
