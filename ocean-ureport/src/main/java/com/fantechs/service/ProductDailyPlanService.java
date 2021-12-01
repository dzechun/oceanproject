package com.fantechs.service;

import com.fantechs.entity.ProductDailyPlanModel;

import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/01
 */
public interface ProductDailyPlanService {
    List<ProductDailyPlanModel> findList(Map<String, Object> map);
}
