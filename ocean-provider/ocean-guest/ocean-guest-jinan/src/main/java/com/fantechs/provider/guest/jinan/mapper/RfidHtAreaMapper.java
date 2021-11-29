package com.fantechs.provider.guest.jinan.mapper;

import com.fantechs.common.base.general.entity.jinan.history.RfidHtArea;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RfidHtAreaMapper extends MyMapper<RfidHtArea> {
    List<RfidHtArea> findHtList(Map<String, Object> map);
}