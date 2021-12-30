package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface BaseStorageMapper extends MyMapper<BaseStorage> {
    List<BaseStorage> findList(Map<String, Object> map);
    //批量更新
    int batchUpdate(List<BaseStorage> baseStorages);

    ///获取物料专用库位列表
    List<StorageRuleDto> findStorageMaterial(Map<String,Object> map);

    List<StorageRuleDto> findPutawayRule(Map<String,Object>map);

    Integer findPutawayNo(@Param("warehouseId")Long warehouseId,@Param("materialId")Long materialId);

    List<StorageRuleDto> BatchEqualStorage(Map<String,Object> map);
    List<StorageRuleDto> EmptyStorage(Map<String ,Object> map);
    List<StorageRuleDto> MixedWithStorage(Map<String ,Object> map);
    List<StorageRuleDto> LastStorage(Map<String ,Object> map);

    List<BaseStorage> findStockDetStorageList(Map<String, Object> map);
}