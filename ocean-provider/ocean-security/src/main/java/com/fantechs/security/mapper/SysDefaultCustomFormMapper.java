package com.fantechs.security.mapper;

import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysDefaultCustomFormMapper extends MyMapper<SysDefaultCustomForm> {
    List<SysDefaultCustomFormDto> findList(Map<String, Object> map);
}