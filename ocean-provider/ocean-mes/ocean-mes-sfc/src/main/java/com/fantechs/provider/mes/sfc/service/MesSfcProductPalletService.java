package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcPalletReportDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/05/08.
 */

public interface MesSfcProductPalletService extends IService<MesSfcProductPallet> {
    List<MesSfcProductPalletDto> findList(Map<String, Object> map);

    /**
     * 获取栈板看板数据
     * @return
     */
    List<MesSfcPalletReportDto> getPalletReport();
}
