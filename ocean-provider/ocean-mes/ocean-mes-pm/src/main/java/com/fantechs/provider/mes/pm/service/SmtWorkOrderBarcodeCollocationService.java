package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by Mr.Lei on 2020/11/21.
 */

public interface SmtWorkOrderBarcodeCollocationService extends IService<SmtWorkOrderBarcodeCollocation> {
    List<SmtWorkOrderBarcodeCollocationDto> findList(SearchSmtWorkOrderBarcodeCollocation searchSmtWorkOrderBarcodeCollocation);
}
