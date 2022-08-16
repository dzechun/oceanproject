package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.PrintBaseStorageCode;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.support.IService;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/23.
 */

public interface BaseStorageService extends IService<BaseStorage> {

    List<BaseStorage> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseStorageImport> baseStorageImports);

    //根据编码进行批量更新
    int batchUpdate(List<BaseStorage> baseStorages);

    //减可放托盘数
    int minusSurplusCanPutSalver(Long storageId,Integer num);

    int plusSurplusCanPutSalver(Long storageId, Integer num);

    List<StorageRuleDto> findStorageMaterial(Map<String,Object> map);

    List<StorageRuleDto> findPutawayRule(Map<String,Object> map);

    Integer findPutawayNo(Long warehouseId,Long materialId);

    List<StorageRuleDto> BatchEqualStorage(Map<String ,Object> map);

    List<StorageRuleDto> EmptyStorage(Map<String ,Object> map);

    List<StorageRuleDto> MixedWithStorage(Map<String ,Object> map);

    List<StorageRuleDto> LastStorage(Map<String ,Object> map);

    int saveByApi(BaseStorage baseStorage);

    int printStorageCode(List<PrintBaseStorageCode> printBaseStorageCodes);
}
