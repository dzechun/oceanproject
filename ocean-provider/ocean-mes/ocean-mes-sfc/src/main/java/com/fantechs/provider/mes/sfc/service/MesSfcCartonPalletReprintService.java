package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcCartonPalletReprintDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcCartonPalletReprint;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/05/19.
 */

public interface MesSfcCartonPalletReprintService extends IService<MesSfcCartonPalletReprint> {
    List<MesSfcCartonPalletReprintDto> findList(Map<String, Object> map);
}
