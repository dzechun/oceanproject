package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBook;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamJigStandingBookService extends IService<EamJigStandingBook> {
    List<EamJigStandingBookDto> findList(Map<String, Object> map);
}
