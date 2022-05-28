package com.fantechs.auth.mapper;

import com.fantechs.common.base.general.dto.security.SysConfigurationDto;
import com.fantechs.common.base.general.dto.security.SysFieldDto;
import com.fantechs.common.base.general.dto.security.SysTableDto;
import com.fantechs.common.base.general.entity.security.SysConfiguration;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysConfigurationMapper extends MyMapper<SysConfiguration> {
    List<SysConfigurationDto> findList(Map<String, Object> map);
    List<SysTableDto> findTbalesByName(@Param("tableName")String tableName);
    int insertSelect(@Param("sqlStr")  String sqlStr);
    List<SysFieldDto> findFieldList(@Param("tableName")String tableName);

}