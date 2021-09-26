package com.fantechs.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.entity.InventoryAge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Mapper
public interface InventoryAgeUreportMapper extends MyMapper<InventoryAge> {
    List<InventoryAge> findList(Map<String, Object> map);
}
