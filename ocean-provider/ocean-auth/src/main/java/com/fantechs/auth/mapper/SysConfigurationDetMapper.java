package com.fantechs.auth.mapper;

import com.fantechs.common.base.general.dto.security.SysConfigurationDetDto;
import com.fantechs.common.base.general.entity.security.SysConfigurationDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysConfigurationDetMapper extends MyMapper<SysConfigurationDet> {
    List<SysConfigurationDetDto> findList(Map<String, Object> map);
    int  batchUpdate(List<SysConfigurationDet> list);
}