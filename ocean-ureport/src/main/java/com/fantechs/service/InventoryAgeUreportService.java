package com.fantechs.service;

import com.fantechs.entity.InventoryAge;

import java.util.List;
import java.util.Map;

public interface InventoryAgeUreportService {
    List<InventoryAge> findList(Map<String, Object> map);
}
