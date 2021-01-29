package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtMaterialSupplierDto;
import com.fantechs.common.base.entity.basic.SmtMaterialSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialSupplier;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/11/03.
 */

public interface SmtMaterialSupplierService extends IService<SmtMaterialSupplier> {

    List<SmtMaterialSupplierDto> findList(SearchSmtMaterialSupplier searchSmtMaterialSupplier);
    Map<String, Object> importExcel(List<SmtMaterialSupplierDto> smtMaterialSupplierDtos);
}
