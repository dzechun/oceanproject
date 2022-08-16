package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionExemptedList;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2020/09/27.
 */

public interface BaseSupplierService extends IService<BaseSupplier> {
    List<BaseSupplier> findInspectionSupplierList(SearchBaseInspectionExemptedList searchBaseInspectionExemptedList);
    List<BaseSupplier> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseSupplierImport> baseSupplierImports, Byte supplierType);
    List<BaseSupplier> findAll(Map<String, Object> map);
    List<BaseHtSupplier> findHtList(Map<String, Object> map);
    int saveByApi (BaseSupplier baseSupplier);

    /**
     * 新增并返回ID
     * @param baseSupplier
     * @return
     */
    Long saveForReturnID(BaseSupplier baseSupplier);
}
