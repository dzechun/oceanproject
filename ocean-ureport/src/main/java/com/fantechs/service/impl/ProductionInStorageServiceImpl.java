package com.fantechs.service.impl;

import com.fantechs.entity.OmInStorage;
import com.fantechs.entity.ProductionInStorage;
import com.fantechs.entity.ProductionInStorageDet;
import com.fantechs.mapper.ProductionInStorageMapper;
import com.fantechs.service.ProductionInStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/24
 */
@Service
public class ProductionInStorageServiceImpl implements ProductionInStorageService {
    @Resource
    private ProductionInStorageMapper productionInStorageMapper;

    @Override
    public List<ProductionInStorage> findProList(Map<String, Object> map) {
        return productionInStorageMapper.findProList(map);
    }

    @Override
    public List<OmInStorage> findOmList(Map<String, Object> map) {
        return productionInStorageMapper.findOmList(map);
    }

    @Override
    public List<ProductionInStorageDet> findProDetList(Map<String, Object> map) {
        return productionInStorageMapper.findProDetList(map);
    }
}
