package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetBarcodeImport;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDetBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */

public interface SrmInAsnOrderDetBarcodeService extends IService<SrmInAsnOrderDetBarcode> {
    List<SrmInAsnOrderDetBarcodeDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SrmInAsnOrderDetBarcodeImport> srmInAsnOrderDetBarcodeImport,Long asnOrderId);
}
