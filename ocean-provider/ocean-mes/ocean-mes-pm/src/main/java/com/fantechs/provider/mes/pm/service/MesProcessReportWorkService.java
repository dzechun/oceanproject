package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesProcessReportWorkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesProcessReportWork;
import com.fantechs.common.base.general.entity.mes.pm.MesProcessReportWork;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/19.
 */

public interface MesProcessReportWorkService extends IService<MesProcessReportWork> {
    List<MesProcessReportWorkDto> findList(SearchMesProcessReportWork searchMesProcessReportWork);
}
