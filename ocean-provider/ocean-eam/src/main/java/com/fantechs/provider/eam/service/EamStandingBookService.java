package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtStandingBook;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamStandingBookService extends IService<EamStandingBook> {
    List<EamStandingBookDto> findList(Map<String, Object> map);
    List<EamHtStandingBook> findHtList(Map<String, Object> map);
}
