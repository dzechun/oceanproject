package com.fantechs.service;

import com.fantechs.entity.OmInStorage;
import com.fantechs.entity.ProductionInStorage;
import com.fantechs.entity.ProductionInStorageDet;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/24
 */
public interface ProductionInStorageService {
    List<ProductionInStorage> findProList(Map<String ,Object> map);

    List<OmInStorage> findOmList(Map<String ,Object> map);

    List<ProductionInStorageDet> findProDetList(Map<String,Object> map);
}
