package com.fantechs.provider.guest.jinan.mapper;

import com.fantechs.common.base.general.entity.jinan.RfidBaseStationLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RfidBaseStationLogMapper extends MyMapper<RfidBaseStationLog> {
    List<RfidBaseStationLog> findList(Map<String, Object> map);
}