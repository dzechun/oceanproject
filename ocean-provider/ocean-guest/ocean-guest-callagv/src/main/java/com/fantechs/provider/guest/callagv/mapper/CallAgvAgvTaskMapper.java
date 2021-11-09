package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvAgvTaskMapper extends MyMapper<CallAgvAgvTask> {
    List<CallAgvAgvTaskDto> findList(Map<String, Object> map);
}