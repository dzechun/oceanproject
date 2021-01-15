package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/23.
 */

public interface SmtWorkOrderBarcodePoolService extends IService<SmtWorkOrderBarcodePool> {

    List<SmtWorkOrderBarcodePoolDto> findList(SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool);
}
