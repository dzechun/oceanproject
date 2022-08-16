package com.fantechs.service;

import com.fantechs.entity.InventoryAge;
import com.fantechs.entity.InventoryAgeDet;

import java.util.List;
import java.util.Map;

public interface InventoryAgeUreportService {
    List<InventoryAge> findList(Map<String, Object> map);

    List<InventoryAgeDet> findDetList(Map<String, Object> map);
}
