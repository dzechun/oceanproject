package com.fantechs.mapper;

import com.fantechs.entity.PackingWarehousingModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Mapper
public interface PackingWarehousingMapper {
    List<PackingWarehousingModel> findList(Map<String ,Object> map);
}
