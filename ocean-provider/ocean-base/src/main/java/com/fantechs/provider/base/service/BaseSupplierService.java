package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2020/09/27.
 */

public interface BaseSupplierService extends IService<BaseSupplier> {
    List<BaseSupplier> findInspectionSupplierList(SearchBaseInspectionExemptedList searchBaseInspectionExemptedList);
    List<BaseSupplier> findList(SearchBaseSupplier searchBaseSupplier);
    Map<String, Object> importExcel(List<BaseSupplierImport> baseSupplierImports, Byte supplierType);

    int addOrUpdate (BaseSupplier baseSupplier);
}
