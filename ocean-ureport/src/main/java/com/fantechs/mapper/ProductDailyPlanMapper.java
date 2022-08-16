package com.fantechs.mapper;

import com.fantechs.entity.ProductDailyPlanModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/01
 */
@Mapper
public interface ProductDailyPlanMapper {
    List<ProductDailyPlanModel> findList(Map<String, Object> map);
}
