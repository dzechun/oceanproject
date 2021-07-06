package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.base.util.StorageRuleDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface BaseStorageMapper extends MyMapper<BaseStorage> {
    List<BaseStorage> findList(Map<String, Object> map);
    //批量更新
    int batchUpdate(List<BaseStorage> baseStorages);

    List<StorageRuleDto> findPutawayRule(Map<String,Object>map);

    Integer findPutawayNo(@Param("warehouseId")Long warehouseId,@Param("materialId")Long materialId);
}