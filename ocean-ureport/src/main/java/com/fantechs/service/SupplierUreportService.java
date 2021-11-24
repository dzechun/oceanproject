package com.fantechs.service;

import com.fantechs.entity.BaseSupplierInfo;

import java.util.List;
import java.util.Map;

public interface SupplierUreportService {
    List<BaseSupplierInfo> findList(Map<String, Object> map);
}
