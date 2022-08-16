package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletRePODto;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface MesSfcProductPalletRePOService extends IService<MesSfcProductPalletRePODto> {

    List<MesSfcProductPalletRePODto> findList(Map<String, Object> map);

    int updateBarcodePO(MesSfcProductPalletRePODto mesSfcProductPalletRePODto) throws Exception;

}
