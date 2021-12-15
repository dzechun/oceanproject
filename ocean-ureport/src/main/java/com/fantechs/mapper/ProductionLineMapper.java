package com.fantechs.mapper;

import com.fantechs.entity.ProductLineTop;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ProductionLineMapper {
    ProductLineTop findTopList(Map<String, Object> map);
}
