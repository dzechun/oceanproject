package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPalletDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/05/08.
 */

public interface MesSfcProductPalletDetService extends IService<MesSfcProductPalletDet> {
    List<MesSfcProductPalletDetDto> findList(Map<String, Object> map);
}
