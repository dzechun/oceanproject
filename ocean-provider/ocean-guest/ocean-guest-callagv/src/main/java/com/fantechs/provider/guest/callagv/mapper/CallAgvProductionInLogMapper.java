package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDetDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvProductionInLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvProductionInLogMapper extends MyMapper<CallAgvProductionInLog> {
    List<CallAgvProductionInLogDto> findList(Map<String, Object> map);

    List<CallAgvProductionInLogDetDto> findDetList(Map<String, Object> map);
}