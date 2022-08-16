package com.fantechs.mapper;

import com.fantechs.entity.WmsInnerInventoryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Mapper
public interface WmsInnerInventoryMapper {
    List<WmsInnerInventoryModel> findList(Map<String,Object> map);
}
