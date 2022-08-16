package com.fantechs.service;


import com.fantechs.common.base.support.IService;
import com.fantechs.dto.StorageMonthEndInventoryDto;
import com.fantechs.entity.StorageMonthEndInventory;

import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/03/02.
 */

public interface StorageMonthEndInventoryService extends IService<StorageMonthEndInventory> {
    List<StorageMonthEndInventoryDto> findList(Map<String, Object> map);

    List<StorageMonthEndInventoryDto> findMonthEndList(Map<String, Object> map);

    int record();
}
