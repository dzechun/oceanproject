package com.fantechs.auth.mapper;

import com.fantechs.common.base.general.dto.security.SysCustomFormDetDto;
import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysCustomFormDetMapper extends MyMapper<SysCustomFormDet> {
   List<SysCustomFormDetDto> findList(Map<String, Object> map);
}
