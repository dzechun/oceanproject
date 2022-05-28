package com.fantechs.auth.mapper;

import com.fantechs.common.base.dto.security.SysApiLogDto;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysApiLogMapper extends MyMapper<SysApiLog> {

    List<SysApiLogDto> findList(Map<String, Object> map);
}