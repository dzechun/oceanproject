package com.fantechs.provider.guest.jinan.mapper;

import com.fantechs.common.base.general.entity.jinan.RfidBaseStation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RfidBaseStationMapper extends MyMapper<RfidBaseStation> {
    List<RfidBaseStation> findList(Map<String, Object> map);
}