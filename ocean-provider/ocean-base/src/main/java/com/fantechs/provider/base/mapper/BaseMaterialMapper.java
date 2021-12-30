package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseMaterialMapper extends MyMapper<BaseMaterial> {

    List<BaseMaterialDto> findList(Map<String, Object> map);

    //根据编码进行批量更新
    int batchUpdateByCode(List<BaseMaterial> baseMaterials);

    List<BaseMaterialDto> findStockDetMaterialList(Map<String, Object> map);
}