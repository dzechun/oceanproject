package com.fantechs.mapper;

import com.fantechs.entity.MonthInOutModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */

@Mapper
public interface MonthInOutMapper {
    List<MonthInOutModel> findInList(Map<String,Object> map);

    List<MonthInOutModel> findOutList(Map<String,Object> map);
}
