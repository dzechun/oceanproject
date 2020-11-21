package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2020/11/21.
 */

public interface SmtWorkOrderBarcodeCollocationService extends IService<SmtWorkOrderBarcodeCollocation> {
    List<SmtWorkOrderBarcodeCollocationDto> findList(SearchSmtWorkOrderBarcodeCollocation searchSmtWorkOrderBarcodeCollocation);
}
