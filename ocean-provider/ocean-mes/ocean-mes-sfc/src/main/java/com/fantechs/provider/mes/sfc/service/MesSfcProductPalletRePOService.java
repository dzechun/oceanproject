package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletRePODto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface MesSfcProductPalletRePOService extends IService<MesSfcBarcodeProcess> {
    List<MesSfcProductPalletRePODto> findList(Map<String, Object> map);

    /**
     * 按PO分组找PO
     * @param map
     * @return
     */
    List<MesSfcBarcodeProcess> findByPOGroup(Map<String, Object> map);

    /**
     * 按PO分组找栈板PO
     * @param map
     * @return
     */
    List<MesSfcBarcodeProcess> findByPalletPOGroup(Map<String, Object> map);

}
