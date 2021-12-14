package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetImport;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */

public interface SrmInAsnOrderDetService extends IService<SrmInAsnOrderDet> {
    List<SrmInAsnOrderDetDto> findList(Map<String, Object> map);

  //  Map<String, Object> importExcel(List<SrmInAsnOrderDetImport> srmInAsnOrderDetImports, Long asnOrderId);

    List<SrmInAsnOrderDetDto> importExcels(List<SrmInAsnOrderDetImport> srmInAsnOrderDetImports);

    int pushDown(List<SrmInAsnOrderDetDto> list);
}
