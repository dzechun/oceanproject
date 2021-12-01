package com.fantechs.service.impl;

import com.fantechs.entity.ProductDailyPlanModel;
import com.fantechs.mapper.ProductDailyPlanMapper;
import com.fantechs.service.ProductDailyPlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/01
 */
@Service
public class ProductDailyPlanServiceImpl implements ProductDailyPlanService {

    @Resource
    private ProductDailyPlanMapper productDailyPlanMapper;

    @Override
    public List<ProductDailyPlanModel> findList(Map<String, Object> map) {
        return productDailyPlanMapper.findList(map);
    }
}
