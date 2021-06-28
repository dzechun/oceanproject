package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.base.util.StorageRuleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface BaseStorageMapper extends MyMapper<BaseStorage> {
    List<BaseStorage> findList(Map<String, Object> map);
    //批量更新
    int batchUpdate(List<BaseStorage> baseStorages);

    /**
     * 获取上架专用库位
     * @param warehouseId 仓库id
     * @param materialId 物料id
     * @return 库位列表
     */
    List<StorageRuleDto> findPutawayRule(@Param("warehouseId")Long warehouseId, @Param("materialId")Long materialId);
}