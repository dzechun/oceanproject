package com.fantechs.provider.imes.storage.service.impl;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.storage.mapper.SmtStorageInventoryMapper;
import com.fantechs.provider.imes.storage.service.SmtStorageInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/02.
 */
@Service
public class SmtStorageInventoryServiceImpl  extends BaseService<SmtStorageInventory> implements SmtStorageInventoryService {

    @Resource
    private SmtStorageInventoryMapper smtStorageInventoryMapper;

    @Override
    public List<SmtStorageInventoryDto> findList(Map<String,Object> map) {
        return smtStorageInventoryMapper.findList(map);
    }
}
