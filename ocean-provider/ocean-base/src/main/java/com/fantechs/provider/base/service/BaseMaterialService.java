package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface BaseMaterialService extends IService<BaseMaterial>{

    List<BaseMaterialDto> findList(Map<String, Object> map);

    /**
     * 初始化盘点-单表查询物料信息
     * @param map
     * @return
     */
    List<BaseMaterial> findListByInitInventory(Map<String, Object> map);

    List<BaseMaterialDto> findAll(Map<String, Object> map);

    //根据编码进行批量更新
    int batchUpdateByCode(List<BaseMaterial> baseMaterials);

    int batchUpdate(List<BaseMaterial> baseMaterials);

    Map<String, Object> importExcel(List<BaseMaterialImport> baseMaterialImports);

    BaseMaterial saveApi(BaseMaterial baseMaterial);

    Map<String, Long> findIdByCode(List<String> materialCodes);

}
