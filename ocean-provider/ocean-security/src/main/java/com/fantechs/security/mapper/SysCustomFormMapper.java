package com.fantechs.security.mapper;

import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysCustomFormMapper extends MyMapper<SysCustomForm> {
    List<SysCustomFormDto> findList(Map<String, Object> map);
}
