package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.imports.SmtStationImport;
import com.fantechs.common.base.dto.basic.imports.SmtSupplierImport;
import com.fantechs.common.base.entity.basic.SmtSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtSupplier;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2020/09/27.
 */

public interface SmtSupplierService extends IService<SmtSupplier> {

    List<SmtSupplier> findList(SearchSmtSupplier searchSmtSupplier);
    Map<String, Object> importExcel(List<SmtSupplierImport> smtSupplierImports);
}
