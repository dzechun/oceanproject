package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventConfig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsWarningEventConfigMapper extends MyMapper<EwsWarningEventConfig> {
    List<EwsWarningEventConfigDto> findList(Map<String,Object> map);
}