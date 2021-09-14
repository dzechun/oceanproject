package com.fantechs.provider.tem.mapper;

import com.fantechs.common.base.general.entity.tem.history.TemHtVehicle;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TemHtVehicleMapper extends MyMapper<TemHtVehicle> {
    List<TemHtVehicle> findList(Map<String, Object> map);
}