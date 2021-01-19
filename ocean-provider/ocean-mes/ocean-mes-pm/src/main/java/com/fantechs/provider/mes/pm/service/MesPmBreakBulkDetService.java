package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/01/18.
 */

public interface MesPmBreakBulkDetService extends IService<MesPmBreakBulkDet> {
    List<MesPmBreakBulkDetDto> findList(Map<String, Object> map);
}
