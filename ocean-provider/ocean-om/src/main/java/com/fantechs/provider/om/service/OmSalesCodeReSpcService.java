package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.imports.OmSalesCodeReSpcImport;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/10/15.
 */

public interface OmSalesCodeReSpcService extends IService<OmSalesCodeReSpc> {
    List<OmSalesCodeReSpcDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<OmSalesCodeReSpcImport> list);
}
