package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulk;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulk;
import com.fantechs.common.base.support.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * @author mr.lei
 * @date 2021-01-18 11:30:42
 */

public interface MesPmBreakBulkService extends IService<MesPmBreakBulk> {
    List<MesPmBreakBulkDto> findList(SearchMesPmBreakBulk searchMesPmBreakBulk);

    MesPmBreakBulk saveBreak(MesPmBreakBulk mesPmBreakBulk);
}
