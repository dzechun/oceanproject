package com.fantechs.security.mapper;

import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface SysDefaultCustomFormDetMapper extends MyMapper<SysDefaultCustomFormDet> {
    List<SysDefaultCustomFormDetDto> findList(Map<String, Object> map);
}