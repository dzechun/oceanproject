package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcDataCollectDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcDataCollect;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */

public interface MesSfcDataCollectService extends IService<MesSfcDataCollect> {
    List<MesSfcDataCollectDto> findList(Map<String, Object> map);
}
