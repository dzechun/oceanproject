package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.entity.basic.BaseWarning;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWarningMapper extends MyMapper<BaseWarning> {

    List<BaseWarningDto> findList(Map<String, Object> map);
}