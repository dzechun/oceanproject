package com.fantechs.provider.imes.storage.service;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */

public interface SmtStorageInventoryDetService extends IService<SmtStorageInventoryDet> {

    List<SmtStorageInventoryDetDto> findList(Map<String, Object> map);

}
