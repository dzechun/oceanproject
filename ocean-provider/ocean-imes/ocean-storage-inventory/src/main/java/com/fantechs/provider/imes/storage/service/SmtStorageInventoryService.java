package com.fantechs.provider.imes.storage.service;


import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/02.
 */

public interface SmtStorageInventoryService extends IService<SmtStorageInventory> {

    List<SmtStorageInventoryDto> findList(Map<String, Object> map);

    int out(SmtStorageInventory smtStorageInventory);
}
