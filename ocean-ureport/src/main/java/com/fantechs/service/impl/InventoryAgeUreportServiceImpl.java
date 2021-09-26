package com.fantechs.service.impl;

import com.fantechs.common.base.support.BaseService;
import com.fantechs.entity.InventoryAge;
import com.fantechs.mapper.InventoryAgeUreportMapper;
import com.fantechs.service.InventoryAgeUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class InventoryAgeUreportServiceImpl extends BaseService<InventoryAge> implements InventoryAgeUreportService {

    @Resource
    private InventoryAgeUreportMapper inventoryAgeUreportMapper;

    @Override
    public List<InventoryAge> findList(Map<String, Object> map) {
        return inventoryAgeUreportMapper.findList(map);
    }
}
