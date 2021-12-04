package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDetBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface SrmInHtAsnOrderDetBarcodeService extends IService<SrmInHtAsnOrderDetBarcode> {
    List<SrmInHtAsnOrderDetBarcodeDto> findList(Map<String, Object> map);
}
